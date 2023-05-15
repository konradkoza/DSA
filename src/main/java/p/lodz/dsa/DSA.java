package p.lodz.dsa;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class DSA {

    private BigInteger p,q,g,x,y;

    private int keySize;

    private final SecureRandom random=new SecureRandom();

    public boolean keysWereGenerated(){
        return (p != null && q != null && g != null && x != null && y != null);
    }

    public void generateKey() {

        keySize = (random.nextInt(8) + 9) * 64; // generate key size as a multiple of 64 between 576 and 1024 bits, inclusive
        q = BigInteger.probablePrime(160, random);
        p = generateP(q);

        g = computeGenerator(p, q);
        x = generatePrivateKey(q);
        y = computePublicKey(g, x, p);

    }

    private BigInteger generateP(BigInteger q) {
        BigInteger temp1, temp2;
        do
        {
            temp1 = BigInteger.probablePrime(keySize, random);
            temp2 = temp1.subtract(BigInteger.ONE);
            temp1 = temp1.subtract(temp2.remainder(q)); // p = t1 - ((t1-1) % q) => (q | p-1)
        } while (!temp1.isProbablePrime(2));

        return temp1;
    }

    private BigInteger computeGenerator(BigInteger p, BigInteger q) {
        BigInteger pMinusOne = p.subtract(BigInteger.ONE);
        BigInteger h;
        do {
            h = new BigInteger(q.bitLength(), random); // h rzędu q
        } while (!(h.compareTo(BigInteger.ONE) >= 0 || h.compareTo(pMinusOne) <= 0)); // 0 < h < p - 1
        return h.modPow(pMinusOne.divide(q), p); // h ^[(p-1)/q] mod p
    }

    private BigInteger generatePrivateKey(BigInteger q) {
        BigInteger x = new BigInteger(q.bitLength(), random);
        while (x.compareTo(q) < 0) { //  x < q
            x = new BigInteger(q.bitLength(), random);
        }
        return x;
    }

    private BigInteger computePublicKey(BigInteger g, BigInteger x, BigInteger p) {
        BigInteger y = g.modPow(x, p); // y = g^x mod p
        return y;
    }

    public BigInteger[] sign(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {

        BigInteger k;
        BigInteger r;
        BigInteger s;
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hashedData = sha256.digest(data);
        BigInteger m = new BigInteger(1, hashedData);
        do {
            k = new BigInteger(q.bitLength(), random);
        } while (k.compareTo(BigInteger.ZERO) == 0 || k.compareTo(q.subtract(BigInteger.ONE)) > 0);
        r = g.modPow(k, p).mod(q);
        s = k.modInverse(q).multiply(m.add(x.multiply(r))).mod(q);
        BigInteger podpis[]=new BigInteger[2];
        podpis[0]=r;
        podpis[1]=s;
        return podpis;
    }

    public boolean verify(byte[] data, BigInteger[] podpis) throws NoSuchAlgorithmException, InvalidKeyException {
        BigInteger r = podpis[0];
        BigInteger s = podpis[1];
        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0) {
            return false;
        }
        if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
            return false;
        }
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hashedData = sha256.digest(data);
        BigInteger m = new BigInteger(1, hashedData);
        BigInteger w = s.modInverse(q);
        BigInteger u1 = m.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = (g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p).mod(q);
        return v.equals(r);
    }

    //region Getters and Setters
    public BigInteger getP() {
        return p;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public BigInteger getG() {
        return g;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public BigInteger getX() {
        return x;
    }

    public void setX(BigInteger x) {
        this.x = x;
    }

    public BigInteger getY() {
        return y;
    }

    public void setY(BigInteger y) {
        this.y = y;
    }
    //endregion
}
