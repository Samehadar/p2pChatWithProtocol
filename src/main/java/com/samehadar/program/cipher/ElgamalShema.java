package com.samehadar.program.cipher;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class ElgamalShema implements KeyGen<Map>, Encryptor<Map, Map> {

    private Long p;
    private Long g;
    private Long y;
    private Long x;
    Map<String, Long> keys;

    /**
     * Default constructor
     */
    public ElgamalShema() {}

    @Override
    public Map<String, Long> generateKey(Object... args) {
        this.p = (Long)args[0];
        this.g = (Long)args[1];
        this.x = (Long)args[2];
        Long y = exp(g, x, p);
        this.y = y;
        this.keys = new HashMap<String, Long>() {{
            put("p", (Long) args[0]);
            put("g", (Long) args[1]);
            put("x", (Long) args[2]);
            put("y", y);
        }};
        return keys;
    }

    /**
     * Returns open and close keys
     * @return HashMap that contains (p, g, y, x)
     */
    public Map<String, Long> getCipherKeys(){
        return this.keys;
    }

    @Override
    public Map encrypt(String openText, Map keys) {
        //a = g^k mod p
        //b = (y^k) * M mod p
        Long g = (Long)keys.get("g");
        Long p = (Long)keys.get("p");
        Long y = (Long)keys.get("y");
        //TODO:: rename
        Long k = createIntegerLowThanP(p);
        Long a = exp(g, k, p);

       // BigInteger b = new BigInteger(y.toString()).pow(k).multiply(new BigInteger(openText)).mod(new BigInteger(p.toString()));

        BigInteger b = new BigInteger(y.toString());
        b = b.modPow(new BigInteger(k.toString()), new BigInteger(p.toString()));
        b = b.multiply(new BigInteger(openText)).mod(new BigInteger(p.toString()));
//        BigInteger b = BigInteger.valueOf(y);
//        b = b.modPow(BigInteger.valueOf(k), BigInteger.valueOf(p));
//        b = b.multiply(new BigInteger(openText)).mod(BigInteger.valueOf(p));


        Map<String, Long> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b.longValue());
        return result;
    }

    @Override
    public String decrypt(String cipherText, Map keys) {
        Long a = Long.parseLong(cipherText.split("\\|")[0]);
        Long b = Long.parseLong(cipherText.split("\\|")[1]);
        Long p = (Long)keys.get("p");
        Long x = (Long)keys.get("x");
        //b * a^(p-1-x) mod p

//        BigInteger result = new BigInteger(a.toString());
//        Integer prom = p - 1 - x;
//        BigInteger p1x = new BigInteger(prom.toString());
//        result = result.modPow(p1x, new BigInteger(p.toString())).multiply(new BigInteger(b.toString())).mod(new BigInteger(p.toString()));
//        return result.toString();

        return exp(
                        exp(a, p - 1 - x, p)
                                * b, 1L, p
                ).toString();
        //return new BigInteger(exp(a, p - 1 - x, p).toString()).multiply(new BigInteger(b.toString())).mod(new BigInteger(p.toString())).toString();
    }

    public String concatenateCipherText(Map<String, Integer> ab) {
        return ab.get("a")+ "|" + ab.get("b");
    }

    public Map<String, Long> encryption(Integer M) {
        //Выбирается сессионный ключ — случайное целое число k такое, что 1<k<p-1
        Integer k = (int)(Math.random() * 10000000); // заточено под моё P
        return encryption(M, Integer.parseInt(this.p.toString()), Integer.parseInt(this.g.toString())
                , Integer.parseInt(this.y.toString()), k);
    }

    //a = g^k mod p
    //b = (y^k) * M mod p
    public Map<String, Long> encryption(Integer M, Integer p, Integer g, Integer y, Integer k) {
        Long a = exp(Long.parseLong(g.toString()), Long.parseLong(k.toString()), Long.parseLong(p.toString()));
//        Integer b = exp(
//                exp(y, k, p)
//                        * M, 1, p
//        );
        BigInteger b = new BigInteger(y.toString());
        b = b.modPow(new BigInteger(k.toString()), new BigInteger(p.toString()));
        b = b.multiply(new BigInteger(M.toString())).mod(new BigInteger(p.toString()));

        Map<String, Long> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b.longValue());
        return result;
    }

    //M = b * a^(p-1-x) mod p
    public Integer decryption(Integer a, Integer b) {
        return Math.toIntExact(exp(
                exp(Long.parseLong(a.toString()), this.p - 1 - this.x, this.p)
                        * b, 1L, this.p
        ));
    }

    /**
     * Returns e^x(mod m)
     */
    public Long exp(Long e, Long x, Long m) {
//        BigInteger result = new BigInteger(e.toString());
//        result = result.modPow(new BigInteger(x.toString()), new BigInteger(m + ""));
//
//        return result.intValue();
        BigInteger result = BigInteger.valueOf(e);
        result = result.modPow(BigInteger.valueOf(x), BigInteger.valueOf(m));

        return result.longValue();
    }

    private Long createIntegerLowThanP(Long p) {
        return (long)(Math.random() * (p - 1));
    }

    /*
    test method for finding g
     protected BigInteger Calculate_g(System.Numerics.BigInteger P)
                {
                    System.Numerics.BigInteger returnedValue = System.Numerics.BigInteger.Zero;
                            //p=2q-1
                            //тогда в качестве g можно взять любое число для которого справедливы неравенства 1<g<(p-1) и (g^q)mod p!=1
                    System.Numerics.BigInteger q = (P - 1) / 2;
                    Boolean isGFound = false;   //равняется true когда найдено число g

                    for (System.Numerics.BigInteger g = 2; g < P; g++)
                    {
                        if (System.Numerics.BigInteger.ModPow(g,q,p)!=System.Numerics.BigInteger.One)
                        {
                            isGFound = true;
                            returnedValue = g;
                            break;
                        }
                    }

                    //если не удалось найти параметр g
                    if (!isGFound)
                    {
                        throw new ArgumentException(...);
                    }

                    return returnedValue;
                }
     */
}
