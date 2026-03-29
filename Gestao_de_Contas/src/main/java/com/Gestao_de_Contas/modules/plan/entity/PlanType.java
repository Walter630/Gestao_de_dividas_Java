package com.Gestao_de_Contas.modules.plan.entity;

public enum PlanType {
    FREE {
        public int debtLimit() { return 5; }
        public boolean hasReports() { return false; }
        public boolean hasExclusive() { return false; }
        public String displayName() { return "Gratuito"; }
    },
    PRO {
        public int debtLimit() { return 50; }
        public boolean hasReports() { return true; }
        public boolean hasExclusive() { return false; }
        public String displayName() { return "Profissional"; }
    },
    PREMIUM {
        public int debtLimit() { return Integer.MAX_VALUE; }
        public boolean hasReports() { return true; }
        public boolean hasExclusive() { return true; }
        public String displayName() { return "Empresarial"; }
    };

    public abstract int debtLimit();
    public abstract boolean hasReports();
    public abstract boolean hasExclusive();
    public abstract String displayName();
}