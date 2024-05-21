/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import java.sql.Connection;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

/**
 *
 * @author rafae
 */
public class ClienteDaoSql implements ClienteDao {
    private ClienteDaoSql(){
    }
    private static ClienteDaoSql dao;
    public static ClienteDaoSql getClienteDaoSql(){
        if (dao == null )
            return dao = new ClienteDaoSql();
        else
            return dao;
    }  
    private String selectAll = 
        "SELECT "+ 
            "id_cliente, " +
            "nome, " +
            "cpf, " +
            "data_nascimento, " +
            "cartao_credito " +
        "FROM " +
            "clientes ";
    private String selectById = selectAll + " " + 
            "WHERE "+
                "id_cliente=?";
    private String insertCliente = 
        "INSERT INTO " +
            "clientes " +
            "(nome," +
            "cpf," +
            "data_nascimento, " +
            "cartao_credito) " +
        "VALUES" +
            "(?,?,?,?)";
    private String updateCliente = 
        "UPDATE " +
            "clientes " +
        "SET " + 
            "nome=?, " +
            "cpf=?, " +
            "data_nascimento=?, " +
            "cartao_credito=? " +
        "WHERE id_cliente = ?";
    private String deleteById = 
        "DELETE FROM "+
            "clientes "+
        "WHERE id_cliente=?";
    private String deleteAll = 
        "DELETE FROM "+
            "clientes ";
    private final String ressetAIPessoas = "ALTER TABLE clientes AUTO_INCREMENT =1";
    private final String ressetAIContas = "ALTER TABLE contas AUTO_INCREMENT =1";
    @Override
    public void add(Cliente cliente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtAdiciona = connection.prepareStatement(insertCliente, Statement.RETURN_GENERATED_KEYS);
            )
        {
            stmtAdiciona.setString(1, cliente.getNome());
            stmtAdiciona.setString(2, cliente.getCpf());
            stmtAdiciona.setDate(3, Date.valueOf(cliente.getDataNascimento()));
            stmtAdiciona.setString(4, cliente.getCartaoCredito());
            
            stmtAdiciona.execute();
            ResultSet rs = stmtAdiciona.getGeneratedKeys();
            rs.next();
            long i = rs.getLong(1);
            cliente.setId(i);
        }
    }

    @Override
    public List<Cliente> getAll() throws Exception {
                try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetAll = connection.prepareStatement(selectAll);
            ResultSet rs = stmtGetAll.executeQuery();
            )
        {
            List<Cliente> clientes = new ArrayList<Cliente>();
            while (rs.next()){
                clientes.add(new Cliente(rs.getLong("id_cliente"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito")));
            }
            return clientes;
        }
    }

    @Override
    public Cliente getById(long id) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtGetById = connection.prepareStatement(selectById);
            )
        {
            stmtGetById.setLong(1, id);
            try(ResultSet rs = stmtGetById.executeQuery())
            {
                if (rs.next()){
                    long idd = rs.getLong("id_cliente");
                    return new Cliente(rs.getLong("id_cliente"), rs.getString("nome"), rs.getString("cpf"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("cartao_credito"));
                }
                else throw new SQLException("Cliente não encontrado com id="+id);
            }
        }    
    }

    @Override
    public void update(Cliente cliente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtUpdate= connection.prepareStatement(updateCliente);
            )
        {
            stmtUpdate.setString(1, cliente.getNome());
            stmtUpdate.setString(2,cliente.getCpf());
            stmtUpdate.setDate(3, Date.valueOf(cliente.getDataNascimento()));
            stmtUpdate.setString(4, cliente.getCartaoCredito());
            stmtUpdate.setLong(5, cliente.getId());

            stmtUpdate.executeUpdate();


        }       
    }

    @Override
    public void delete(Cliente cliente) throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtExclui = connection.prepareStatement(deleteById);
            )
        {
            stmtExclui.setLong(1, cliente.getId());
            stmtExclui.executeUpdate();
        }      
    }

    @Override
    public void deleteAll() throws Exception {
        try(
            Connection connection = ConnectionFactory.getConnection();
            PreparedStatement stmtExcluiAll = connection.prepareStatement(deleteAll, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement stmtResetaPessoas = connection.prepareStatement(ressetAIPessoas);
            PreparedStatement stmtResetaContas = connection.prepareStatement(ressetAIContas);
            )
        {
            stmtExcluiAll.execute();
            ResultSet rs = stmtExcluiAll.getGeneratedKeys();
            rs.next();
            
            stmtResetaPessoas.executeUpdate();
            stmtResetaContas.executeUpdate();
        }    
    }
    
}
