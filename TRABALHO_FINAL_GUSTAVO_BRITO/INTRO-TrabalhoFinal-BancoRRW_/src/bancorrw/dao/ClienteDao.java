/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import java.util.List;

/**
 *
 * @author rafae
 */
public interface ClienteDao extends Dao<Cliente>{
    public void add(Cliente cliente) throws Exception;
    public List<Cliente> getAll() throws Exception;
    public Cliente getById(long id) throws Exception;
    public void update(Cliente cliente) throws Exception;
    public void delete(Cliente cliente) throws Exception;
    public void deleteAll() throws Exception;
}
