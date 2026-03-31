package com.Gestao_de_Contas.modules.pix.entity;

// PlanoTipo.java
public enum PlanType {

    FREE    ("Free",    0.00, "FREE"),
    PRO     ("Pro",     29.99, "PRO"),
    PREMIUM ("Premium", 49.99, "PREMIUM"),;

    private final String nome;
    private final Double valor;
    private final String txid;

    PlanType(String nome, Double valor,  String txid) {
        this.nome = nome;
        this.valor = valor;
        this.txid = txid;
    }

    public String getNome()  { return nome; }
    public Double getValor() { return valor; }
    public String getTxid()  { return txid; }
}