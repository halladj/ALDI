import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.*;

public class Rdp implements Serializable{
	private int nbPlace;
	private int nbTransition; 
	private int [] M0;
	private int [][] Pre;
	private int [][] Post;
	private ArrayList<int[]> marquageList= new ArrayList<int[]>();

	public Rdp(){
	}

	public Rdp(int nbPlace, int nbTransition, int [] M0,	int [][] Pre, int [][] Post){
		this.nbPlace  = nbPlace;
		this.nbTransition  = nbTransition; 
		this.M0   = M0;
		this.Pre  = Pre;
		this.Post = Post;
	}

	public Rdp loadRdp(String fileName){
		Rdp r = null;
		try {
			ObjectInputStream flotLecture = new ObjectInputStream(new FileInputStream(fileName));
			Object obj = flotLecture.readObject();
			if (obj instanceof Rdp) {
				r =(Rdp)obj;
				flotLecture.close();
			}
			else {
				System.out.println("Erreur de type");
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de la lecture du fichier :" + fileName);
		}
		return r;
	}

	public void displayRDP(){
		System.out.println("Marquage initial : "+Arrays.toString(M0));
		System.out.println("Matrice Pre : ");
		displayMatrix(Pre);
		System.out.println("Matrice Post : ");
		displayMatrix(Post);
	}

	public void displayMatrix(int[][] mat) {
		for (int[] ligne : mat) {
			System.out.println(Arrays.toString(ligne));
		}
	}

	public String displayMarquageList() {
		String s = "";
		for (int i = 0; i < marquageList.size(); i++)
			s = s + Arrays.toString(marquageList.get(i)) + " , ";
		return s;
	}	

	public int[] getM0(){
		return M0;
	}

	public void addMarquage(int []M) {
		marquageList.add(M);
	}
	
	public int t_franchissable(int t, int []M){
		/* une transition t est franchissable pour un marquage M, ssi, M(pi)≥Pré(pi, t)*/
		for (int p = 0; p < M.length; p++){
			if (M[p]<Pre[p][t]){
				return 0;
			}
		}
		return 1;
	}

	public int[] succ(int t, int []M){
		// nM(p) = M(p) – Pré(p, t) + Post(p, t)*/
		int[] nM = new int[M.length];
		for (int p = 0; p < M.length; p++){
			nM[p] = M[p] - Pre[p][t] + Post[p][t];
		}
		return nM;		
	}//succ

	public void Generate_Graphe_Marquage_Centralisé(){
		System.out.println("Generation du graphe de marquage centralisé");
	}//Generer_Graphe_Marquage

	public void Generate_Graphe_Marquage_Distribué(){
		System.out.println("Generation du graphe de marquage distribué :");
	}//Generer_G_Marquage_Distribué

	public int hachage(int []M, int nbAgent){
		try {
			// Initialisation de l'algorithme MD5
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Conversion du tableau d'entiers en tableau de bytes
			ByteBuffer buffer = ByteBuffer.allocate(M.length * 4);
			for (int num : M) {
				buffer.putInt(num);
			}
			// Application du hachage MD5
			byte[] digest = md.digest(buffer.array());
			// Conversion en BigInteger
			BigInteger bigInt = new BigInteger(1, digest);
			// Appliquer le modulo nbAgent
			return bigInt.mod(BigInteger.valueOf(nbAgent)).intValue();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return -1; // Valeur d'erreur
		}
	}//hachage

	public void saveRdp(Rdp r, String fileName){
		try{
			ObjectOutputStream flotEcriture = new ObjectOutputStream(new FileOutputStream(fileName));
			flotEcriture.writeObject(r);
			flotEcriture.close();
		} catch (IOException e) {
			System.out.println("Erreur lors de la création du fichier :" + fileName);
		}   
	}

	public boolean equals(int[] M1, int[] M2){
		for (int i = 0; i <M1.length; i++){
			if (M1[i] != M2[i])
				return false;
		}
		return true; 
	}
	
	public int searchMarquage(int[] M){
		for(int i = 0; i < marquageList.size(); i++){
			int[] marquage = (int[]) marquageList.get(i);
			if (equals(M,marquage))
				return i; 
		}	
		return -1;
	}

	public static void main(String[] args) {
		int nbPlace = 5 ;
		int nbTransition = 4; 
		int [] M0 = {1, 0, 0, 0, 0};		
		int [][] Pre = {{1, 0, 0, 0},{0, 1, 0, 0},{0, 0, 1, 0},{0, 0, 0, 1},{0, 0, 0, 1}} ;
		int [][] Post = {{0, 0, 0, 1},{1, 0, 0, 0},{1, 0, 0, 0},{0, 1, 0, 0},{0, 0, 1, 0}};

		Rdp rdp = new Rdp(nbPlace, nbTransition, M0, Pre, Post);
		rdp.displayRDP();
		rdp.saveRdp(rdp, "1erRdp.dat");
	
	}
}