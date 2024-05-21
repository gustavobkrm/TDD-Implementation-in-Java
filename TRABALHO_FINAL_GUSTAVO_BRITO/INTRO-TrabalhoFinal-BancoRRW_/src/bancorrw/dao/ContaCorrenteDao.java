/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaCorrente;
import java.util.List;

/**
 *
 * @author rafae
 */
public interface ContaCorrenteDao extends Dao<ContaCorrente>{

   public void add(ContaCorrente contaCorrente) throws Exception;
    
    public List<ContaCorrente> getAll() throws Exception;
    
    public ContaCorrente getById(long id) throws Exception;
    
    public void update(ContaCorrente contaCorrente) throws Exception;
    
    public void delete(ContaCorrente contaCorrente) throws Exception;
    
    public void deleteAll() throws Exception;
    
    public ContaCorrente getContaCorrenteByCliente(Cliente cliente) throws Exception;
    
}
