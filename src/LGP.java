import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;

public class LGP {

    int productos;
    int tiendas;
    int []unidades;
    double []deliverys;
    double [][]precios;
    int [][]disponibilidad;
    int PopulationSize;
    int MaxEvaluations;
    int Evaluations;
    Solucion[] poblacion;

    public LGP(String Instancia) throws IOException
    {
        FileReader fr=new FileReader(Instancia);
        BufferedReader bf=new BufferedReader(fr);
        bf.readLine(); //linea encabezado
        StringTokenizer temp=new StringTokenizer(bf.readLine(),",");
        productos=Integer.valueOf(temp.nextToken());
        tiendas=Integer.valueOf(temp.nextToken());
        bf.readLine(); //linea encabezado
        unidades=new int[productos];
        for(int i=0;i<productos;i++)
        {
            temp=new StringTokenizer(bf.readLine(),",");
            unidades[Integer.valueOf(temp.nextToken())]=Integer.valueOf(temp.nextToken());
        }
        bf.readLine(); //linea encabezado
        deliverys=new double[tiendas];
        for(int i=0;i<tiendas;i++)
        {
            temp=new StringTokenizer(bf.readLine(),",");
            deliverys[Integer.valueOf(temp.nextToken())]=Double.valueOf(temp.nextToken());
        }
        bf.readLine(); //linea encabezado
        precios=new double[tiendas][productos];
        for(int i=0;i<tiendas;i++)
        {
            temp=new StringTokenizer(bf.readLine(),",");
            for(int j=0;j<productos;j++)
            {
                precios[i][j]=Double.valueOf(temp.nextToken());
            }
        }
        bf.readLine(); //linea encabezado
        disponibilidad=new int[tiendas][productos];
        for(int i=0;i<tiendas;i++)
        {
            temp=new StringTokenizer(bf.readLine(),",");
            for(int j=0;j<productos;j++)
            {
                disponibilidad[i][j]=Integer.valueOf(temp.nextToken());
            }
        }
        PopulationSize=100;
        MaxEvaluations=25000;
        Evaluations=0;
        poblacion=new Solucion[PopulationSize*2];
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }
    public void Ejecutar()
    {
        InicializarPoblacion();
        while(Evaluations<MaxEvaluations)
        {
            int hijosiguiente=PopulationSize;
            for(int x=0;x<PopulationSize/2;x++)
            {
                int padre1=TorneoBinario();
                int padre2=TorneoBinario();
                Solucion[] hijos=Cruza(poblacion[padre1],poblacion[padre2]);
                Muta(hijos[0]);
                Muta(hijos[1]);
                Reparar(hijos[0]);
                Reparar(hijos[1]);
                hijos[0].EvaluaObjetivos();
                hijos[1].EvaluaObjetivos();
                Evaluations+=2;
                poblacion[hijosiguiente++]=hijos[0];
                poblacion[hijosiguiente++]=hijos[1];
            }
            SeleccionAmbiental();
            System.out.println("Evaluaciones "+Evaluations+" valor objetivo "+poblacion[0].objetivo1);
        }
    }

    private void SeleccionAmbiental()
    {
        Arrays.sort(poblacion,new Comparador());//se usan referencias pero no hay problema
    }
    private int TorneoBinario()
    {
        Random rnd=new Random();
        int padre1=rnd.nextInt(PopulationSize);
        int padre2=rnd.nextInt(PopulationSize);
        if(poblacion[padre1].objetivo1<poblacion[padre2].objetivo1)
        {
            return padre1;
        }else {
            return padre2;
        }
    }
    private void InicializarPoblacion()
    {
        for(int i=0;i<PopulationSize;i++)
        {
            poblacion[i]=new Solucion(tiendas,productos,disponibilidad,precios,deliverys,unidades);
            poblacion[i].LlenadoAleatorio();
            Reparar(poblacion[i]);
            poblacion[i].EvaluaObjetivos();
            Evaluations++;
        }
    }
    private Solucion[] Cruza(Solucion a, Solucion b)
    {
        Solucion[] hijos=new Solucion[2];
        hijos[0]=new Solucion(a.NumTiendas,a.NumProductos,a.disponibilidad,a.precios,a.deliverys,a.unidades);
        hijos[1]=new Solucion(a.NumTiendas,a.NumProductos,a.disponibilidad,a.precios,a.deliverys,a.unidades);
        ArrayList<int[]> genes=new ArrayList<>();
        for(int i=0;i<a.cromosoma_instrucciones.size();i++)
        {
            int []copia=new int[3];
            for(int j=0;j<3;j++)
            {
                copia[j]=a.cromosoma_instrucciones.get(i)[j];
            }
            genes.add(copia);
        }

        for(int i=0;i<b.cromosoma_instrucciones.size();i++)
        {
            int []copia=new int[3];
            for(int j=0;j<3;j++)
            {
                copia[j]=b.cromosoma_instrucciones.get(i)[j];
            }
            genes.add(copia);
        }

        for(int i=0;i<genes.size();i++)
        {
            if(Math.random()<0.5)
            {
                hijos[0].cromosoma_instrucciones.add(genes.get(i));
            }else{
                hijos[1].cromosoma_instrucciones.add(genes.get(i));
            }
        }

        return hijos;

    }
    private void Muta(Solucion a)
    {
        double probabilidadMuta=0.05;
        Random rnd=new Random();
        for(int j=0;j<a.cromosoma_instrucciones.size();j++)
        {
                if(Math.random()<probabilidadMuta)
                {
                    a.cromosoma_instrucciones.get(j)[2]=rnd.nextInt(disponibilidad[a.cromosoma_instrucciones.get(j)[0]][a.cromosoma_instrucciones.get(j)[1]]);
                }
        }
    }
    private void Reparar(Solucion a)
    {
        for(int j = 0; j<a.getS()[0].length; j++)//productos
        {
            int sum=0;
            for(int i = 0; i<a.getS().length; i++) //tiendas
            {
                    if(a.getS()[i][j]>disponibilidad[i][j])
                    {
                        a.getS()[i][j]=disponibilidad[i][j];
                    }
                    sum = sum + a.getS()[i][j];
            }
            //System.out.println("Part1 requeridas "+unidades[j]+" compradas "+sum);

            Random rnd=new Random();
            while(sum<unidades[j])
            {
                int pos=rnd.nextInt(a.NumTiendas);
                if(disponibilidad[pos][j]>a.getS()[pos][j]) {
                    a.getS()[pos][j]++;
                    sum++;
                }
            }

            while(sum>unidades[j])
            {
                int pos=rnd.nextInt(a.NumTiendas);
                if(a.getS()[pos][j]>0) {
                    a.getS()[pos][j]--;
                    sum--;
                }
            }

        }
        ArrayList<int []> instrucciones=new ArrayList<>();
        for(int i=0;i< a.NumTiendas;i++)
        {
            for(int j=0;j<a.NumProductos;j++)
            {
                if(a.S[i][j]>0) {
                    int[] gen = new int[3];
                    gen[0] = i;
                    gen[1] = j;
                    gen[2] = a.S[i][j];
                    instrucciones.add(gen);
                }
            }
        }

        a.cromosoma_instrucciones=instrucciones;

    }

    public void printOutput() throws IOException
    {
        poblacion[0].EvaluaObjetivos();
        System.out.println("Valor Objetivo encontrado "+poblacion[0].objetivo1);
        FileOutputStream fos=new FileOutputStream("VAR.txt");
        PrintStream ps=new PrintStream(fos);
        for(int i=0;i<tiendas;i++)
        {
            for(int j=0;j<productos;j++)
            {
                ps.print(poblacion[0].getS()[i][j]+" ");
            }ps.println();
        }
        FileOutputStream fos2=new FileOutputStream("FUN.txt");
        PrintStream ps2=new PrintStream(fos2);
        ps2.println(poblacion[0].objetivo1);
    }
}
