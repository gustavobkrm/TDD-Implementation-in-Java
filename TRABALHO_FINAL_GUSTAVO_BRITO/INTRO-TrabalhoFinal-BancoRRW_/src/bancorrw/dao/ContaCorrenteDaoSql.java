/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaCorrente;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafae
 */
public class ContaCorrenteDaoSql implements ContaCorrenteDao{
    private ContaCorrenteDaoSql(){
    }
    private static ContaCorrenteDaoSql dao;
    public static ContaCorrenteDaoSql getContaCorrenteDaoSql(){
        if (dao == null )
            return dao = new ContaCorrenteDaoSql();
        else
            return dao;
    } 
    private String insertContaCorrente = 
        "INSERT INTO " +
            "contas_corrente " +
            "(id_conta," +
            "limite," +
            "taxa_juros_limite) " +
        "VALUES" +
            "(?,?,?)";
    private String insertConta = 
        "INSERT INTO " +
            "contas " +
            "(id_cliente," +
            "saldo) " +
        "VALUES" +
            "(?,?)";
    
    private String updateClienteIdContaCorrente = 
        "UPDATE " +
            "clientes " +
        "SET " + 
            "id_conta_corrente=? " +
        "WHERE id_cliente = ?";
    private String updateContaCorrente = 
        "UPDATE " +
            "contas_corrente " +
        "SET " + 
            "limite=? ," +
            "taxa_juros_limite=? " +
        "WHERE id_conta = ?";    
    private String updateConta = 
        "UPDATE " +
            "contas " +
        "SET " + 
            "saldo=? " +
        "WHERE id_conta = ?";    
    private String selectByCliente = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente "+
                        "WHERE "+
                            "contas.id_cliente=?";
        private String selectById = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente "+
                        "WHERE "+
                            "contas.id_conta=?";
    private String selectAll = 
                        "SELECT "+
                            "contas_corrente.id_conta, "+
                            "saldo, "+
                            "limite, "+
                            "taxa_juros_limite, "+
                            "clientes.id_cliente,"+
                            "nome, "+
                            "cpf, "+
                            "data_nascimento, "+
                            "cartao_credito "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta "+
                        "INNER JOIN "+
                            "clientes "+
                        "ON "+
                            "contas.id_conta=clientes.id_conta_corrente ";   
    private String deleteById = 
                        "DELETE FROM "+
                            "contas " +       
                        "WHERE " +
                            "id_conta=?";
    private String deleteAll = 
                        "DELETE " +
                            "contas,contas_corrente "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_corrente "+
                        "ON "+
                            "contas.id_conta=contas_corrente.id_conta ";            
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";
    @Override
    public void add(ContaCorrente contaCorrente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtAdicionaConta = connection.prepareStatement(insertConta, Statement.RETURN_GENERATED_KEYS);

            PreparedStatement stmtAdicionaContaCorrente = connection.prepareStatement(insertContaCorrente, Statement.RETURN_GENERATED_KEYS);
                
            PreparedStatement stmtAtualizaContaCorrenteCliente = connection.prepareStatement(updateClienteIdContaCorrente);

            )
        {
            
            stmtAdicionaConta.setLong(1, contaCorrente.getCliente().getId());
            stmtAdicionaConta.setDouble(2, contaCorrente.getSaldo());
            stmtAdicionaConta.execute();            
            
            ResultSet rs = stmtAdicionaConta.getGeneratedKeys();
            if (rs.next()) {
                long idConta = rs.getLong(1);
                contaCorrente.setId(idConta);
            }
            
            stmtAdicionaContaCorrente.setLong(1, contaCorrente.getId());
            stmtAdicionaContaCorrente.setDouble(2, contaCorrente.getLimite());
            stmtAdicionaContaCorrente.setDouble(3, contaCorrente.getTaxaJurosLimite());
            stmtAdicionaContaCorrente.execute();
            
            stmtAtualizaContaCorrenteCliente.setLong(1, contaCorrente.getId());
            stmtAtualizaContaCorrenteCliente.setLong(2, contaCorrente.getCliente().getId());

            stmtAtualizaContaCorrenteCliente.executeUpdate();

        }    
    }

    @Override
    public List<ContaCorrente> getAll() throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetAll = connection.prepareStatement(selectAll);
            ResultSet rs = stmtGetAll.executeQuery();
            )
        {
            List<ContaCorrente> contas = new ArrayList<ContaCorrente>();
            while (rs.next()){

                Cliente c = new Cliente(rs.getLong("id_cliente"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito"));
                contas.add(new ContaCorrente(rs.getDouble("limite"), rs.getDouble("taxa_juros_limite"), rs.getLong("id_conta"),c, rs.getDouble("saldo")));
                
            }
            return contas;
        }    
    }

    @Override
    public ContaCorrente getById(long id) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetById = connection.prepareStatement(selectById);
            )
        {
            stmtGetById.setLong(1, id);
            try(ResultSet rs = stmtGetById.executeQuery())
            {
                if (rs.next()){
                    Cliente c = new Cliente(rs.getLong("id_conta"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito"));
                    return new ContaCorrente(rs.getDouble("limite"), rs.getDouble("taxa_juros_limite"), rs.getLong("id_conta"), c,  rs.getDouble("saldo"));
                }
                else throw new SQLException("");
            }
        }      
    }

    @Override
    public void update(ContaCorrente contaCorrente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtUpdateConta = connection.prepareStatement(updateConta);

            PreparedStatement stmtUpdateContaCorrente = connection.prepareStatement(updateContaCorrente);
                
            )
        {
            stmtUpdateConta.setDouble(1, contaCorrente.getSaldo());
            stmtUpdateConta.setLong(2, contaCorrente.getId());
            stmtUpdateConta.executeUpdate();

            stmtUpdateContaCorrente.setDouble(1, contaCorrente.getLimite());
            stmtUpdateContaCorrente.setDouble(2, contaCorrente.getTaxaJurosLimite());
            stmtUpdateContaCorrente.setDouble(3, contaCorrente.getId());
            stmtUpdateContaCorrente.executeUpdate();          
        }
        
    }

    @Override
    public void delete(ContaCorrente contaCorrente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtExclui = connection.prepareStatement(deleteById);
            )
        {
            stmtExclui.setLong(1, contaCorrente.getId());
            stmtExclui.executeUpdate();
        } 
    }

    @Override
    public void deleteAll() throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtExclui = connection.prepareStatement(deleteAll);
            PreparedStatement stmtReseta = connection.prepareStatement(ressetAIContas);

            )
        {
            stmtExclui.executeUpdate();
            stmtReseta.executeUpdate();
        }     
    }

    @Override
    public ContaCorrente getContaCorrenteByCliente(Cliente cliente) throws Exception{
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetById = connection.prepareStatement(selectByCliente);
            )
        {
            stmtGetById.setLong(1, cliente.getContaCorrente().getCliente().getId());
            try(ResultSet rs = stmtGetById.executeQuery())
            {
                if (rs.next()){
                    return new ContaCorrente(rs.getDouble("limite"), rs.getDouble("taxa_juros_limite"), rs.getLong("id_conta"), cliente,  rs.getDouble("saldo"));
                }
                else throw new SQLException("");
            }
        }   
    }
    
}
