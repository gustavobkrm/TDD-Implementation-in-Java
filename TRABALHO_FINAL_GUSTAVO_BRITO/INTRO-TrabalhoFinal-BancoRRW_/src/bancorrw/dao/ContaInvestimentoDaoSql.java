/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaInvestimento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rafae
 */
public class ContaInvestimentoDaoSql implements ContaInvestimentoDao{
    private ContaInvestimentoDaoSql(){
    }
    private static ContaInvestimentoDaoSql dao;
    public static ContaInvestimentoDaoSql getContaInvestimentoDaoSql(){
        if(dao==null)
            return dao = new ContaInvestimentoDaoSql();
        else
            return dao;
    } 
    private String insertContaInvestimento = 
        "INSERT INTO " +
            "contas_investimento " +
            "(id_conta," +
            "taxa_remuneracao_investimento," +
            "montante_minimo," +
            "deposito_minimo) " +
        "VALUES" +
            "(?,?,?,?)";
    private String insertConta = 
        "INSERT INTO " +
            "contas " +
            "(id_cliente," +
            "saldo) " +
        "VALUES" +
            "(?,?)";
    private String selectAll = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente ";
    private String selectById = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente "+
        "WHERE "+
            "contas.id_conta=?";
    private String selectByCliente = 
        "SELECT "+
            "contas_investimento.id_conta, "+
            "saldo, "+
            "taxa_remuneracao_investimento, "+
            "montante_minimo, "+
            "deposito_minimo, "+
            "clientes.id_cliente,"+
            "nome, "+
            "cpf, "+
            "data_nascimento, "+
            "cartao_credito "+
        "FROM "+
            "contas "+
        "INNER JOIN "+
            "contas_investimento "+
        "ON "+
            "contas.id_conta=contas_investimento.id_conta "+
        "INNER JOIN "+
            "clientes "+
        "ON "+
            "contas.id_cliente=clientes.id_cliente "+
        "WHERE "+
            "contas.id_cliente=?";

    private String updateContaInvestimento = 
        "UPDATE " +
            "contas_investimento " +
        "SET " + 
            "taxa_remuneracao_investimento=? ," +
            "montante_minimo=? ," +            
            "deposito_minimo=? " +
        "WHERE id_conta = ?";    
    private String updateConta = 
        "UPDATE " +
            "contas " +
        "SET " + 
            "saldo=? " +
        "WHERE id_conta = ?";   
    private String deleteById = 
                        "DELETE FROM "+
                            "contas " +
                        "WHERE " +
                            "id_conta=?";
    private String deleteAll = 
                        "DELETE " +
                            "contas,contas_investimento "+
                        "FROM "+
                            "contas "+
                        "INNER JOIN "+
                            "contas_investimento "+
                        "ON "+
                            "contas.id_conta=contas_investimento.id_conta ";     
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";

    @Override
    public void add(ContaInvestimento conta) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtAdicionaConta = connection.prepareStatement(insertConta, Statement.RETURN_GENERATED_KEYS);

            PreparedStatement stmtAdicionaContaInvestimento = connection.prepareStatement(insertContaInvestimento, Statement.RETURN_GENERATED_KEYS);
                
            )   
        {
            stmtAdicionaConta.setLong(1, conta.getCliente().getId());
            stmtAdicionaConta.setDouble(2, conta.getSaldo());
            stmtAdicionaConta.execute();            
            
            ResultSet rs = stmtAdicionaConta.getGeneratedKeys();
            if (rs.next()) {
                long idConta = rs.getLong(1);
                conta.setId(idConta);
            }

            stmtAdicionaContaInvestimento.setLong(1, conta.getId());
            stmtAdicionaContaInvestimento.setDouble(2, conta.getTaxaRemuneracaoInvestimento());
            stmtAdicionaContaInvestimento.setDouble(3, conta.getMontanteMinimo());
            stmtAdicionaContaInvestimento.setDouble(4, conta.getDepositoMinimo());
            stmtAdicionaContaInvestimento.execute();
            
        } 
            
    }

    @Override
    public List<ContaInvestimento> getAll() throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetAll = connection.prepareStatement(selectAll);
            ResultSet rs = stmtGetAll.executeQuery();
            )
        {
            List<ContaInvestimento> contas = new ArrayList<ContaInvestimento>();
            while (rs.next()){

                Cliente c = new Cliente(rs.getLong("id_cliente"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito"));
                contas.add(new ContaInvestimento(
                    rs.getDouble("taxa_remuneracao_investimento"),
                    rs.getDouble("montante_minimo"),
                    rs.getDouble("deposito_minimo"),
                    rs.getDouble("saldo"),
                    rs.getLong("id_conta"),
                    c));
            }
            return contas;
        }        
    }

    @Override
    public ContaInvestimento getById(long id) throws Exception {
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
                    ContaInvestimento contaInvestimento = new ContaInvestimento(
                    rs.getDouble("taxa_remuneracao_investimento"),
                    rs.getDouble("montante_minimo"),
                    rs.getDouble("deposito_minimo"),
                    rs.getDouble("saldo"),
                    rs.getLong("id_conta"),
                    c
                );
                    return contaInvestimento;
                }
                else throw new SQLException("");
            }
        }        
    }

    @Override
    public void update(ContaInvestimento conta) throws Exception {
                    Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtUpdateConta = connection.prepareStatement(updateConta);

            PreparedStatement stmtUpdateContaInvestimento = connection.prepareStatement(updateContaInvestimento);
                
        {
            stmtUpdateConta.setDouble(1, conta.getSaldo());
            stmtUpdateConta.setLong(2, conta.getId());
            stmtUpdateConta.executeUpdate();

            stmtUpdateContaInvestimento.setDouble(1, conta.getTaxaRemuneracaoInvestimento());
            stmtUpdateContaInvestimento.setDouble(2, conta.getMontanteMinimo());
            stmtUpdateContaInvestimento.setDouble(3, conta.getDepositoMinimo());
            stmtUpdateContaInvestimento.setDouble(4, conta.getId());
            stmtUpdateContaInvestimento.executeUpdate();          
        }
    }

    @Override
    public void delete(ContaInvestimento conta) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtExclui = connection.prepareStatement(deleteById);
            )
        {
            stmtExclui.setLong(1, conta.getId());
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
    public List<ContaInvestimento> getContasInvestimentoByCliente(Cliente cliente) throws Exception  {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetAllByCliente = connection.prepareStatement(selectByCliente);
            )
        {
            stmtGetAllByCliente.setLong(1, cliente.getId());
            try(ResultSet rs = stmtGetAllByCliente.executeQuery()){
                List<ContaInvestimento> contas = new ArrayList<ContaInvestimento>();
                while (rs.next()){
                Cliente c = new Cliente(rs.getLong("id_cliente"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito"));
                contas.add(new ContaInvestimento(
                    rs.getDouble("taxa_remuneracao_investimento"),
                    rs.getDouble("montante_minimo"),
                    rs.getDouble("deposito_minimo"),
                    rs.getDouble("saldo"),
                    rs.getLong("id_conta"),
                    c));
            }
            return contas;
            }
        }
    }
    
}
