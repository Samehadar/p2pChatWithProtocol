package com.samehadar.program.cipher;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElgamalShema implements KeyGen<Map> {

    private Integer p;
    private Integer g;
    private Integer y;
    private Integer x;
    Map<String, Integer> keys;

    /**
     * Default constructor
     */
    public ElgamalShema() {}

    public ElgamalShema(Integer p, Integer g, Integer x){
        this.p = p;
        this.g = g;
        this.x = x;
        this.y = exp(g, x, p);
    }

    @Override
    public Map<String, Integer> generateKey(Object... args) {
        this.p = (Integer)args[0];
        this.g = (Integer)args[1];
        this.x = (Integer)args[2];
        Integer y = exp(g, x, p);
        this.y = y;
        this.keys = new HashMap<String, Integer>() {{
            put("p", (Integer) args[0]);
            put("g", (Integer) args[1]);
            put("x", (Integer) args[2]);
            put("y", y);
        }};
        return keys;
    }

    //e^x mod m
    public Integer exp(Integer e, Integer x, Integer m) {
        BigInteger result = new BigInteger(e.toString());
        result = result.modPow(new BigInteger(x.toString()), new BigInteger(m + ""));

        return result.intValue();
    }

    /**
     * Return open and close keys
     * @return HashMap that contains (p, g, y, x)
     */
    public Map<String, Integer> getKeys(){
        return this.keys;
    }

    public Map<String, Integer> encryption(Integer M) {
        //Выбирается сессионный ключ — случайное целое число k такое, что 1<k<p-1
        Integer k = (int)(Math.random() * 10000000); // заточено под моё P
        return encryption(M, this.p, this.g, this.y, k);
    }

    //M = b * a^(p-1-x) mod p
    public Integer decryption(Integer a, Integer b) {
        return exp(
                exp(a, this.p - 1 - this.x, this.p)
                        * b, 1, this.p
        );
    }

    //a = g^k mod p
    //b = (y^k) * M mod p
    public Map<String, Integer> encryption(Integer M, Integer p, Integer g, Integer y, Integer k) {
        Integer a = exp(g, k, p);
//        Integer b = exp(
//                exp(y, k, p)
//                        * M, 1, p
//        );
        BigInteger b = new BigInteger(y.toString());
        b = b.modPow(new BigInteger(k.toString()), new BigInteger(p.toString()));
        b = b.multiply(new BigInteger(M.toString())).mod(new BigInteger(p.toString()));

        Map<String, Integer> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b.intValue());
        return result;
    }
}
