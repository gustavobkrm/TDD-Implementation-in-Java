/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.conta;

import bancorrw.cliente.Cliente;

/**
 *
 * @author rafae
 */
public class ContaCorrente extends Conta{
    private double limite;
    private double taxaJurosLimite;

    public ContaCorrente(double limite, double taxaJurosLimite, long id, Cliente cliente, double saldo) throws Exception {
        super(id, cliente, saldo);
        this.limite = limite;
        this.taxaJurosLimite = taxaJurosLimite;
        cliente.setContaCorrente(this);

    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public double getTaxaJurosLimite() {
        return taxaJurosLimite;
    }

    public void setTaxaJurosLimite(double taxaJurosLimite) {
        this.taxaJurosLimite = taxaJurosLimite;
    }

    public void deposita(double valor){
        
        if (valor > 0)
            super.setSaldo((super.getSaldo() + valor));
        else
            throw new RuntimeException("Valor do depósito não pode ser negativo ou zero. Valor=-50.0");
    }
    
    public void saca(double valor){
        
        if (valor < 0)
            throw new RuntimeException("Valor do saque não pode ser negativo ou zero. Valor=-100.0");
        
        if ((super.getSaldo() + this.limite) >= valor) {
            super.setSaldo((super.getSaldo() - valor));
        }
        else{
            throw new RuntimeException("Saldo insuficiente na conta."+
                            "\nValor saque=1300.0"+
                            "\nSaldo="+this.getSaldo()+
                            "\nLimite="+this.getLimite());
        }
    }
    
    public void aplicaJuros(){
        if (super.getSaldo() < 0)
            super.setSaldo(super.getSaldo() + (super.getSaldo() * this.taxaJurosLimite));
    }
    
    public long getNumero(){
        return super.getId();
    }
}
