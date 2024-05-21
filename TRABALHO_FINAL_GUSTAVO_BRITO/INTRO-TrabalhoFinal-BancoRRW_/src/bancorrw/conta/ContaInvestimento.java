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
public class ContaInvestimento extends Conta{
        private double taxaRemuneracaoInvestimento;
        private double montanteMinimo;
        private double depositoMinimo;

    public ContaInvestimento(double taxaRemuneracaoInvestimento, double montanteMinimo, double depositoMinimo, double saldo, long id, Cliente cliente) {
        super(id, cliente, saldo);
        this.taxaRemuneracaoInvestimento = taxaRemuneracaoInvestimento;
        this.montanteMinimo = montanteMinimo;
        this.depositoMinimo = depositoMinimo;
        cliente.setContasInvestimento(this);
        
        if (montanteMinimo > saldo)throw new RuntimeException ("Saldo não pode ser menor que montante mínimo.");
    }

    public double getTaxaRemuneracaoInvestimento() {
        return taxaRemuneracaoInvestimento;
    }

    public void setTaxaRemuneracaoInvestimento(double taxaRemuneracaoInvestimento) {
        this.taxaRemuneracaoInvestimento = taxaRemuneracaoInvestimento;
    }

    public double getMontanteMinimo() {
        return montanteMinimo;
    }

    public void setMontanteMinimo(double montanteMinimo) {
        this.montanteMinimo = montanteMinimo;
    }

    public double getDepositoMinimo() {
        return depositoMinimo;
    }

    public void setDepositoMinimo(double depositoMinimo) {
        this.depositoMinimo = depositoMinimo;
    }

    public void deposita(double valor){
        if (valor >= this.depositoMinimo)
            super.setSaldo(super.getSaldo() + valor);
        else
            throw new RuntimeException("Valor do depóstio não atingiu o mínimo. Valor Depósito="+valor+" Depóstio Mínimo="+this.depositoMinimo);
    }

    public void saca(double valor){
        if (valor <= 0)
            throw new RuntimeException("Valor do saque não pode ser negativo ou zero. Valor=-100.0");
        
        if (super.getSaldo() - valor >= this.montanteMinimo) {
            super.setSaldo(super.getSaldo() - valor);
        }
        else{
            throw new RuntimeException("Saldo insuficiente para saque. Valor Saque="+valor+" Saldo="+super.getSaldo()+ " Montante Minimo="+this.getMontanteMinimo());
        }
    }
    public void aplicaJuros(){
        if (super.getSaldo() > 0)
            super.setSaldo(super.getSaldo() + (super.getSaldo() * this.taxaRemuneracaoInvestimento));
    }
    
    public long getNumero(){
        return super.getId();
    }
}
