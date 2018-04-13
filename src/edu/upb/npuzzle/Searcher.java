package edu.upb.npuzzle;

import java.util.*;

public class Searcher {

    private PuzzleState root;
    //private List< PuzzleState > frontier;
    private Queue< PuzzleState > frontier;
    private Set< PuzzleState > explored;
    private int nodeCounter;
    private long expanding;
    private long ordering;
    private long foreach;
    private long heuristica;
    private long initialTime;

    public Searcher( PuzzleState root ) {
        this.root = root;
    }

    public PuzzleState findSolution() {
        nodeCounter = 0;

        expanding = 0L;
        ordering = 0L;
        foreach = 0L;
        heuristica = 0L;
        initialTime = System.nanoTime();

        //frontier = new LinkedList<>();
        frontier= new PriorityQueue<>(  ( n1, n2 ) -> {
            long startTime2 = System.nanoTime();//

            int f_n1 = n1.getCost() + n1.heuristic();
            int f_n2 = n2.getCost() + n2.heuristic();

            heuristica += System.nanoTime() - startTime2; //

            return Integer.compare(f_n1, f_n2);} );
        explored = new HashSet<>();
        frontier.add( root );
        long startTime;
        do {
            if ( frontier == null ) {
                return null;
            }
            startTime = System.nanoTime(); //
            PuzzleState current = frontier.remove();
            /*PuzzleState current = frontier.stream().min(  ( n1, n2 ) -> {
                long startTime2 = System.nanoTime();//

                int f_n1 = n1.getCost() + n1.heuristic();
                int f_n2 = n2.getCost() + n2.heuristic();

                heuristica += System.nanoTime() - startTime2; //

                return Integer.compare(f_n1, f_n2);
            }  ).get();*/
            ordering += System.nanoTime() - startTime; //
            //frontier.remove( current );
            explored.add(current);

            if ( current.isGoalState() ) {
                long totalTimeSeconds = (System.nanoTime() - initialTime )/1000000000L;
                System.out.println("Solution found at depth "+current.depth()+" in " +totalTimeSeconds+" seconds");
                return current;
            }
            startTime = System.nanoTime(); //
            List<PuzzleState> children = current.expandChildren();
            expanding += System.nanoTime() - startTime; //
            nodeCounter++;

            startTime = System.nanoTime(); //
            for (PuzzleState child : children) {

                if ( !explored.contains( child ) ) {

                    frontier.add( child );

                }
            }
            foreach += System.nanoTime() - startTime; //


            /*frontier.sort( ( n1, n2 ) -> {
                long startTime2 = System.nanoTime();//

                int f_n1 = n1.getCost() + n1.heuristic();
                int f_n2 = n2.getCost() + n2.heuristic();

                heuristica += System.nanoTime() - startTime2; //

                if ( f_n1 == f_n2 ) {
                    return 0;
                }
                if ( f_n1 < f_n2 ) {
                    return 1;
                }
                return -1;
            } ); */
            if ( nodeCounter %1000 ==0 ){
                System.out.println("expanding="+expanding+" foreach="+foreach+" ordering=" +ordering+ " heuristica="+heuristica+"  #nodes=" + nodeCounter);
                System.out.println("elIf="+PuzzleState.elIf+" elMod="+PuzzleState.elMod+" elCiclo=" +(PuzzleState.elCiclo - (PuzzleState.elMod + PuzzleState.elIf)));

            }
        } while ( true );
    }

}
