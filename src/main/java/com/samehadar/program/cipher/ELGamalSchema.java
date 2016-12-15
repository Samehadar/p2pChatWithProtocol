package com.samehadar.program.cipher;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ELGamalSchema implements KeyGen<Map>, Cypher<Map, Map> {

    private BigInteger p;
    private BigInteger g;
    private BigInteger y;
    private BigInteger x;
    Map<String, BigInteger> keys;

    private Random secureRandom;

    /**
     * Default constructor
     */
    public ELGamalSchema() {
        this.secureRandom = new SecureRandom();
    }

    @Override
    public Map<String, BigInteger> generateKey(Object... args) {
        this.p = parseBigInteger(args[0]);
        this.g = parseBigInteger(args[1]);
        this.x = parseBigInteger(args[2]);
        BigInteger y = exp(g, x, p);
        this.y = y;
        this.keys = new HashMap<String, BigInteger>() {{
            put("p", parseBigInteger(args[0]));
            put("g", parseBigInteger(args[1]));
            put("x", parseBigInteger(args[2]));
            put("y", y);
        }};
        return keys;
    }

    /**
     * Returns open and close keys
     * @return HashMap that contains (p, g, y, x)
     */
    public Map<String, BigInteger> getCipherKeys(){
        return this.keys;
    }

    /**
     * Returns current secureRandom
     * @return Random
     */
    public Random getSecureRandom() {
        return this.secureRandom;
    }

    @Override
    public Map encrypt(String openText, Map keys) {
        //a = g^k mod p
        //b = (y^k) * M mod p
        BigInteger g = (BigInteger)keys.get("g");
        BigInteger p = (BigInteger)keys.get("p");
        BigInteger y = (BigInteger)keys.get("y");
        BigInteger m = new BigInteger(openText);
        BigInteger k = createBigIntegerLowThanP(p);
        BigInteger a = exp(g, k, p);

        BigInteger b = m.multiply(y.modPow(k, p)).mod(p);

        Map<String, BigInteger> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b);
        return result;
    }

    @Override
    public String decrypt(String cipherText, Map keys) {
        BigInteger a = new BigInteger(cipherText.split("\\|")[0]);
        BigInteger b = new BigInteger(cipherText.split("\\|")[1]);
        BigInteger p = (BigInteger)keys.get("p");
        BigInteger x = (BigInteger)keys.get("x");
        //b * a^(p-1-x) mod p
        //or b*(a)^(-1) mod p - this is used

        BigInteger axmodp = a.modPow(x, p);
        BigInteger aInverse = axmodp.modInverse(p);
        BigInteger ad = aInverse.multiply(b).mod(p);
        return ad.toString();
    }

    public String concatenateCipherText(Map<String, BigInteger> ab) {
        return ab.get("a") + "|" + ab.get("b");
    }

    /**
     * Returns e^x(mod m)
     */
    private BigInteger exp(BigInteger e, BigInteger x, BigInteger m) {
        return e.modPow(x, m);
    }

    private BigInteger createBigIntegerLowThanP(BigInteger p) {
        Integer startBit = 64;
        BigInteger result = new BigInteger(startBit, this.secureRandom);
        while (result.compareTo(p) != -1) {
            startBit--;
            result = new BigInteger(startBit, this.secureRandom);
        }
        return result;
    }

    private BigInteger parseBigInteger(Object x) {
        if (x instanceof BigInteger){
            return (BigInteger) x;
        } else if (x instanceof Integer) {
            return new BigInteger((x).toString());
        } else if (x instanceof String) {
            return new BigInteger((String)x);
        }
        return null;
    }

    /*
    test method for finding g - первообразный корень для P
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
