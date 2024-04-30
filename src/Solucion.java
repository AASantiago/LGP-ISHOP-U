import java.util.ArrayList;
import java.util.Random;

public class Solucion{
    int NumTiendas;
    int NumProductos;
    int [][] S;
    int [][]disponibilidad;
    double [][]precios;
    double []deliverys;
    int []unidades;
    double objetivo1;
    ArrayList<int[]> cromosoma_instrucciones;

    public Solucion(int NumTiendas, int NumProductos, int[][] disponibilidad, double[][] precios, double[] deliverys, int[] unidades)
    {
        this.NumTiendas=NumTiendas;
        this.NumProductos=NumProductos;
        this.deliverys=new double[NumTiendas];
        this.unidades=new int[NumProductos];
        S =new int[NumTiendas][NumProductos];
        this.disponibilidad=new int[NumTiendas][NumProductos];
        this.precios=new double[NumTiendas][NumProductos];
        cromosoma_instrucciones=new ArrayList<>();
        for(int i=0;i<NumTiendas;i++)
        {
            for(int j=0;j<NumProductos;j++)
            {
                this.disponibilidad[i][j]=disponibilidad[i][j];
                this.precios[i][j]=precios[i][j];
            }
        }

        for(int i=0;i<NumTiendas;i++)
        {
            this.deliverys[i]=deliverys[i];
        }
        for(int i=0;i<NumProductos;i++)
        {
            this.unidades[i]=unidades[i];
        }

    }

    public int[][] getS()
    {
        return S;
    }

    public void EvaluaObjetivos()
    {
        //esta parte resetea la solucion a ceros
        for(int i=0;i<NumTiendas;i++)
        {
            for(int j=0;j<NumProductos;j++)
            {
                S[i][j]=0;
            }
        }

        //esta parte llena la solucion con las instrucciones
        for(int i=0;i<cromosoma_instrucciones.size();i++)
        {
            int pos1=cromosoma_instrucciones.get(i)[0];
            int pos2=cromosoma_instrucciones.get(i)[1];
            int val=cromosoma_instrucciones.get(i)[2];
            S[pos1][pos2]=val;
        }

        //ESTA PARTE EVALUA LA FUNCION OBJETIVO
        objetivo1=0.0;
        boolean secompro_tiendas[]=new boolean[NumTiendas];
        for(int i=0;i<NumTiendas;i++)
        {
            secompro_tiendas[i]=false;
        }
        for(int i=0;i<NumTiendas;i++)
        {
            for(int j=0;j<NumProductos;j++)
            {
                objetivo1+= S[i][j]*precios[i][j];
                if(S[i][j]>0) {
                    secompro_tiendas[i] = true;
                }
            }
        }
        for(int i=0;i<NumTiendas;i++)
        {
            if(secompro_tiendas[i])
            {
                objetivo1+=deliverys[i];
            }
        }

    }
    public void LlenadoAleatorio()
    {
        Random rnd=new Random();
        cromosoma_instrucciones=new ArrayList<>();
        int num=rnd.nextInt(NumTiendas*NumProductos);
        for(int i=0;i<num;i++)
        {
            int[] gen=new int[3];
            gen[0]=rnd.nextInt(NumTiendas);
            gen[1]=rnd.nextInt(NumProductos);
            gen[2]=rnd.nextInt(disponibilidad[gen[0]][gen[1]]+1);
            cromosoma_instrucciones.add(gen);
        }

        for(int i=0;i<NumTiendas;i++)
        {
            for(int j=0;j<NumProductos;j++)
            {
                S[i][j]=0;
            }
        }
        for(int i=0;i<cromosoma_instrucciones.size();i++)
        {
            S[cromosoma_instrucciones.get(i)[0]][cromosoma_instrucciones.get(i)[1]]=cromosoma_instrucciones.get(i)[2];
        }

    }



}
