package com.classes;

public enum TypeOfPayment {
    Visa,
    AmericanExpress,
    BancomatG,
    BancomatP,
    Contante;

    @Override
    public String toString() {
        switch(this) {
        case Visa: return "Visa";
        case AmericanExpress: return "American Experess";
        case BancomatG: return "Bancomat Gabriele";
        case BancomatP: return "Bancomat Patrizia";
        case Contante: return "Contanti";
        default: throw new IllegalArgumentException();
        }
  }
}
