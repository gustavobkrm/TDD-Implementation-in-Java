/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.dao;

import bancorrw.cliente.Cliente;
import bancorrw.conta.ContaInvestimento;
import java.util.List;


/**
 *
 * @author rafae
 */
public interface ContaInvestimentoDao extends Dao<ContaInvestimento>{

public void add(ContaInvestimento conta) throws Exception;
    
    public List<ContaInvestimento> getAll() throws Exception;
    
    public ContaInvestimento getById(long id) throws Exception;
    
    public void update(ContaInvestimento conta) throws Exception;
    
    public void delete(ContaInvestimento conta) throws Exception;
    
    public void deleteAll() throws Exception;
    
    public List<ContaInvestimento> getContasInvestimentoByCliente(Cliente cliente) throws Exception;
    
}
