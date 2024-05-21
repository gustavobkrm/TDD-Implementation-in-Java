/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bancorrw.cliente;

import bancorrw.conta.ContaCorrente;
import bancorrw.conta.ContaInvestimento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rafae
 */
public class Cliente extends Pessoa{

    private List<ContaInvestimento> contasInvestimento;
    private ContaCorrente contaCorrente;
    private String cartaoCredito;

    public Cliente(long id, String nome, String cpf, LocalDate dataNascimento, String cartaoCredito) {
        super(id, nome, cpf, dataNascimento);
        this.cartaoCredito = cartaoCredito;
    }

    public String getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(String cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
    }

    public List<ContaInvestimento> getContasInvestimento() {
        return contasInvestimento;
    }

    public void setContasInvestimento(ContaInvestimento contasInvestimento) {
        if (this.contasInvestimento == null)
                this.contasInvestimento = new ArrayList<ContaInvestimento>();
        this.contasInvestimento.add(contasInvestimento);
    }

    public ContaCorrente getContaCorrente() {
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrente contaCorrente){
        if (this.contaCorrente != null && (contaCorrente.getId() == this.contaCorrente.getId() && this.contaCorrente.getSaldo() > 0 && this.contaCorrente.getSaldo() != contaCorrente.getSaldo()) && (this.contaCorrente.getCliente() == contaCorrente.getCliente())) {
            throw new RuntimeException("Não pode modificar a conta corrente, pois saldo da original não está zerado. "
                    + "Para fazer isso primeiro zere o saldo da conta do cliente. Saldo=" + this.contaCorrente.getSaldo());
        } else {
            this.contaCorrente = contaCorrente;
        }
    }
    
    public double getSaldoTotalCliente(){
        double somaSaldo = 0;
        for(ContaInvestimento c :this.getContasInvestimento()){
            somaSaldo += c.getSaldo();
        }
        return somaSaldo;
    }
}
