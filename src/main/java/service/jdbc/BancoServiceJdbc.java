/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.jdbc;

import exception.AutenticacionException;
import exception.FondosInsuficientesException;
import model.Cliente;
import model.Cuenta;
import model.EnumTipo;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author XPC
 */
public class BancoServiceJdbc implements service.IBancoService {

    // --------- API que usará la UI ---------

    @Override
    public Cuenta loginPorPin(String numCuenta, String pin) throws AutenticacionException {
        try (var cn = ConnectionFactory.get()) {
            var ps = cn.prepareStatement("""
                SELECT c.id, c.saldo, c.pin_hash, c.pin_salt,
                       cli.id as cli_id, cli.nombre, cli.apellido, cli.email, cli.telefono, cli.direccion
                  FROM cuenta c
                  JOIN cliente cli ON cli.id = c.cliente_id
                 WHERE c.numero = ?
                 LIMIT 1
            """);
            ps.setString(1, numCuenta);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) throw new AutenticacionException("Cuenta no encontrada");

                var pinHash = rs.getBytes("pin_hash");
                var pinSalt = rs.getBytes("pin_salt");
                if (!PinHasher.verify(pin.toCharArray(), pinSalt, pinHash)) {
                    throw new AutenticacionException("PIN incorrecto");
                }

                var cli = new Cliente(
                        rs.getInt("cli_id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                var cuenta = new Cuenta(numCuenta, cli, pin, rs.getBigDecimal("saldo").doubleValue());
                return cuenta;
            }
        } catch (AutenticacionException ae) {
            throw ae;
        } catch (Exception e) {
            throw new RuntimeException("Error de login JDBC", e);
        }
    }

    @Override
    public Cuenta abrirCuenta(Cliente cli, String pin) {
        try (var cn = ConnectionFactory.get()) {
            cn.setAutoCommit(false);
            try {
                long clienteId = insertOrGetCliente(cn, cli);
                String numero = String.valueOf(System.currentTimeMillis());

                byte[] salt = PinHasher.salt();
                byte[] hash = PinHasher.hash(pin.toCharArray(), salt);

                var insCuenta = cn.prepareStatement("""
                    INSERT INTO cuenta(numero, cliente_id, pin_hash, pin_salt, saldo, estado)
                    VALUES (?,?,?,?,0.00,'ACTIVA')
                """);
                insCuenta.setString(1, numero);
                insCuenta.setLong(2, clienteId);
                insCuenta.setBytes(3, hash);
                insCuenta.setBytes(4, salt);
                insCuenta.executeUpdate();

                cn.commit();

                // Devuelve cuenta "de dominio" para la UI
                return new Cuenta(numero, cli, pin, 0.0);
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            throw new RuntimeException("Email ya registrado", dup);
        } catch (Exception e) {
            throw new RuntimeException("Error abriendo cuenta", e);
        }
    }

    @Override
    public BigDecimal depositar(String numero, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Monto debe ser > 0");

        try (var cn = ConnectionFactory.get()) {
            cn.setAutoCommit(false);
            cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                RowCuenta row = selectCuentaForUpdate(cn, numero);
                BigDecimal nuevo = row.saldo.add(monto);

                updateSaldo(cn, row.id, nuevo);
                insertTx(cn, row.id, EnumTipo.DEPOSITO.name(), monto, true, null, "Depósito");

                cn.commit();
                return nuevo;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en depósito", e);
        }
    }

    @Override
    public BigDecimal retirar(String numero, BigDecimal monto) throws FondosInsuficientesException {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Monto debe ser > 0");

        try (var cn = ConnectionFactory.get()) {
            cn.setAutoCommit(false);
            cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                RowCuenta row = selectCuentaForUpdate(cn, numero);
                if (row.saldo.compareTo(monto) < 0) {
                    insertTx(cn, row.id, EnumTipo.RETIRO.name(), monto, false, null, "Saldo insuficiente");
                    cn.rollback();
                    throw new FondosInsuficientesException("Saldo insuficiente");
                }

                BigDecimal nuevo = row.saldo.subtract(monto);
                updateSaldo(cn, row.id, nuevo);
                insertTx(cn, row.id, EnumTipo.RETIRO.name(), monto, true, null, "Retiro");

                cn.commit();
                return nuevo;
            } catch (FondosInsuficientesException fie) {
                throw fie;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (FondosInsuficientesException fie) {
            throw fie;
        } catch (Exception e) {
            throw new RuntimeException("Error en retiro", e);
        }
    }

    @Override
    public BigDecimal transferir(String origen, String destino, BigDecimal monto) throws FondosInsuficientesException {
        if (origen.equals(destino)) throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        if (monto.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Monto debe ser > 0");

        try (var cn = ConnectionFactory.get()) {
            cn.setAutoCommit(false);
            cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                String a = (origen.compareTo(destino) <= 0) ? origen : destino;
                String b = (origen.compareTo(destino) <= 0) ? destino : origen;

                RowCuenta A = selectCuentaForUpdate(cn, a);
                RowCuenta B = selectCuentaForUpdate(cn, b);

                RowCuenta ro = A.numero.equals(origen) ? A : B;
                RowCuenta rd = A.numero.equals(destino) ? A : B;

                if (ro.saldo.compareTo(monto) < 0) {
                    insertTx(cn, ro.id, EnumTipo.TRANSFERENCIA.name(), monto, false, rd.id, "Saldo insuficiente");
                    cn.rollback();
                    throw new FondosInsuficientesException("Saldo insuficiente");
                }

                updateSaldo(cn, ro.id, ro.saldo.subtract(monto));
                updateSaldo(cn, rd.id, rd.saldo.add(monto));

                insertTx(cn, ro.id, EnumTipo.TRANSFERENCIA.name(), monto, true,  rd.id, "Transferencia enviada");
                insertTx(cn, rd.id, EnumTipo.TRANSFERENCIA.name(), monto, true,  ro.id, "Transferencia recibida");

                cn.commit();
                return ro.saldo.subtract(monto);
            } catch (FondosInsuficientesException fie) {
                throw fie;
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        } catch (FondosInsuficientesException fie) {
            throw fie;
        } catch (Exception e) {
            throw new RuntimeException("Error en transferencia", e);
        }
    }

    @Override
    public BigDecimal consultarSaldo(String numero) {
        try (var cn = ConnectionFactory.get()) {
            var ps = cn.prepareStatement("SELECT saldo FROM cuenta WHERE numero=?");
            ps.setString(1, numero);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Cuenta no encontrada");
                return rs.getBigDecimal(1);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error consultando saldo", e);
        }
    }

    /** Devuelve filas básicas para la tabla de historial: fecha, tipo, monto, estado */
    @Override
    public List<Object[]> listarHistorialBasico(String numero) {
        try (var cn = ConnectionFactory.get()) {
            long cuentaId = getCuentaId(cn, numero);
            var ps = cn.prepareStatement("""
                SELECT fecha, tipo, monto, estado
                  FROM transaccion
                 WHERE cuenta_id = ?
                 ORDER BY fecha DESC, id DESC
            """);
            ps.setLong(1, cuentaId);
            var out = new ArrayList<Object[]>();
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Object[]{
                        rs.getTimestamp("fecha"),
                        Enum.valueOf(EnumTipo.class, rs.getString("tipo")),
                        rs.getBigDecimal("monto"),
                        rs.getString("estado")
                    });
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("Error listando historial", e);
        }
    }

    /** Última transacción de una cuenta (para Comprobante) */
    @Override
    public Object[] ultimaTransaccionBasica(String numero) {
        try (var cn = ConnectionFactory.get()) {
            long cuentaId = getCuentaId(cn, numero);
            var ps = cn.prepareStatement("""
                SELECT id, fecha, tipo, monto, estado
                  FROM transaccion
                 WHERE cuenta_id = ?
                 ORDER BY fecha DESC, id DESC
                 LIMIT 1
            """);
            ps.setLong(1, cuentaId);
            try (var rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Object[]{
                    rs.getLong("id"),
                    rs.getTimestamp("fecha"),
                    Enum.valueOf(EnumTipo.class, rs.getString("tipo")),
                    rs.getBigDecimal("monto"),
                    rs.getString("estado")
                };
            }
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo última transacción", e);
        }
    }
    

    @Override
    public void actualizarCliente(model.Cliente c) {
        try (var cn = ConnectionFactory.get()) {
            var ps = cn.prepareStatement("""
                UPDATE cliente
                  SET nombre=?, apellido=?, email=?, telefono=?, direccion=?
                 WHERE id=?
            """);
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getTelefono());
           ps.setString(5, c.getDireccion());
            ps.setInt(6, c.getIdCliente());
            if (ps.executeUpdate() != 1) throw new RuntimeException("Cliente no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando cliente", e);
        }
    }

    @Override
    public void actualizarCuenta(model.Cuenta cuenta) {
        String numero = cuenta.getNumCuenta();
        String pin = cuenta.getPIN();
        if (pin == null || pin.isBlank()) return;
        try (var cn = ConnectionFactory.get()) {
            byte[] salt = PinHasher.salt();
            byte[] hash = PinHasher.hash(pin.toCharArray(), salt);
            var ps = cn.prepareStatement("""
                UPDATE cuenta
                   SET pin_hash=?, pin_salt=?
                 WHERE numero=?
            """);
            ps.setBytes(1, hash);
            ps.setBytes(2, salt);
            ps.setString(3, numero);
            if (ps.executeUpdate() != 1) throw new RuntimeException("Cuenta no encontrada");
        } catch (Exception e) {
            throw new RuntimeException("Error actualizando cuenta", e);
        }
    }

    // --------- Helpers internos ---------

    private static final class RowCuenta {
        long id;
        String numero;
        BigDecimal saldo;
    }

    private RowCuenta selectCuentaForUpdate(Connection cn, String numero) throws SQLException {
        var ps = cn.prepareStatement("SELECT id, numero, saldo FROM cuenta WHERE numero=? FOR UPDATE");
        ps.setString(1, numero);
        try (var rs = ps.executeQuery()) {
            if (!rs.next()) throw new SQLException("Cuenta no encontrada: " + numero);
            RowCuenta r = new RowCuenta();
            r.id = rs.getLong("id");
            r.numero = rs.getString("numero");
            r.saldo = rs.getBigDecimal("saldo");
            return r;
        }
    }

    private void updateSaldo(Connection cn, long cuentaId, BigDecimal nuevo) throws SQLException {
        var ps = cn.prepareStatement("UPDATE cuenta SET saldo=? WHERE id=?");
        ps.setBigDecimal(1, nuevo);
        ps.setLong(2, cuentaId);
        ps.executeUpdate();
    }

    private void insertTx(Connection cn, long cuentaId, String tipo, BigDecimal monto, boolean ok,
                          Long cuentaDestId, String detalle) throws SQLException {
        var ps = cn.prepareStatement("""
           INSERT INTO transaccion(cuenta_id, tipo, monto, estado, cuenta_destino_id, detalle)
           VALUES (?,?,?,?,?,?)
        """);
        ps.setLong(1, cuentaId);
        ps.setString(2, tipo);
        ps.setBigDecimal(3, monto);
        ps.setString(4, ok ? "EXITOSA" : "FALLIDA");
        if (cuentaDestId == null) ps.setNull(5, Types.BIGINT); else ps.setLong(5, cuentaDestId);
        ps.setString(6, detalle);
        ps.executeUpdate();
    }

    private long getCuentaId(Connection cn, String numero) throws SQLException {
        var ps = cn.prepareStatement("SELECT id FROM cuenta WHERE numero=?");
        ps.setString(1, numero);
        try (var rs = ps.executeQuery()) {
            if (!rs.next()) throw new SQLException("Cuenta no encontrada: " + numero);
            return rs.getLong(1);
        }
    }

    private long insertOrGetCliente(Connection cn, Cliente c) throws SQLException {
        // Intenta insertar; si el email ya existe, recupera su id.
        var ins = cn.prepareStatement("""
            INSERT INTO cliente(nombre, apellido, email, telefono, direccion)
            VALUES (?,?,?,?,?)
        """, Statement.RETURN_GENERATED_KEYS);
        try {
            ins.setString(1, c.getNombre());
            ins.setString(2, c.getApellido());
            ins.setString(3, c.getEmail());
            ins.setString(4, c.getTelefono());
            ins.setString(5, c.getDireccion());
            ins.executeUpdate();
            try (var gk = ins.getGeneratedKeys()) {
                gk.next();
                return gk.getLong(1);
            }
        } catch (SQLIntegrityConstraintViolationException dup) {
            var sel = cn.prepareStatement("SELECT id FROM cliente WHERE email=?");
            sel.setString(1, c.getEmail());
            try (var rs = sel.executeQuery()) {
                if (!rs.next()) throw dup;
                return rs.getLong(1);
            }
        }
    }

    private static final class Holder { static final BancoServiceJdbc I = new BancoServiceJdbc(); }
    public static BancoServiceJdbc getInstance() { return Holder.I; }
}

