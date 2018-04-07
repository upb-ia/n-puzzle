package edu.upb.npuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PuzzleState {

    int[][] board;
    private int size;
    List <PuzzleState> children;
    PuzzleState parent;
    private int cost;
    public static long elIf, elCiclo, elMod;//
    static {
        elIf=0L;
        elCiclo=0L;
        elMod=0L;
    }



    public PuzzleState() {
        init( 3 );
        this.parent = null;
        cost = 0;
    }

    public PuzzleState( int[][] board, PuzzleState parent) {
        this.board = board;
        this.size = board.length;
        this.parent = parent;
        cost = parent.cost + 1;
    }

    private void init( int size ) {
        Random random = new Random();

        this.size = size;
        board = new int[size][size];
        for (int i=0;i<size;i++) {
            Arrays.fill( board[ i ], 0);
        }

        int maxNum = size * size - 1;
        for ( int a = 1; a <= maxNum; a++ ) {
            int i, j;
            do {
                i = random.nextInt( size );
                j = random.nextInt( size );
            } while ( board[ i ][ j ] != 0 );
            board[i][j] = a;
        }
    }

    public List<PuzzleState> expandChildren() {
        children = new ArrayList<>( 4 );
        for(int i = 0; i< size; i++) {
            for(int j=0; j< size; j++) {
                if ( board[i][j] == 0 ) {
                    if(i > 0) {
                        int[][] childBoard = cloneBoard();
                        childBoard [i][j] = childBoard [i-1][j];
                        childBoard [i-1][j] = 0;
                        PuzzleState child = new PuzzleState( childBoard , this);
                        children.add( child );
                    }
                    if(i < size-1) {
                        int[][] childBoard = cloneBoard();
                        childBoard [i][j] = childBoard [i+1][j];
                        childBoard [i+1][j] = 0;
                        PuzzleState child = new PuzzleState( childBoard , this);
                        children.add( child );
                    }if(j > 0) {
                        int[][] childBoard = cloneBoard();
                        childBoard [i][j] = childBoard [i][j-1];
                        childBoard [i][j-1] = 0;
                        PuzzleState child = new PuzzleState( childBoard , this);
                        children.add( child );
                    }
                    if(j < size-1) {
                        int[][] childBoard = cloneBoard();
                        childBoard [i][j] = childBoard [i][j+1];
                        childBoard [i][j+1] = 0;
                        PuzzleState child = new PuzzleState( childBoard , this);
                        children.add( child );
                    }

                }
            }
        }
        return children;
    }

    public int[][] cloneBoard() {
        int[][] clonedBoard = new int[size][size];
        for(int i = 0; i< size; i++) {
            for(int j=0; j< size; j++) {
                clonedBoard[i][j] = board[i][j];
            }
        }
        return clonedBoard;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        PuzzleState that = ( PuzzleState ) o;
        return Arrays.equals( board, that.board );
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode( board );
    }

    public int getCost() {

        return cost;
    }

    public int heuristic() {
        int maxNum = size*size;
        int index = 1, count = 0;
        long startTime;
        long startTimeFor = System.nanoTime();
        for ( int i = 0; i < size; i++ ) {
            for ( int j = 0; j < size; j++ ) {
                startTime = System.nanoTime();
                if ( board[i][j] != index) {
                    count++;
                }
                elIf += System.nanoTime() - startTime;
                index++;
                startTime = System.nanoTime();
                index %= maxNum;
                elMod += System.nanoTime() - startTime;
            }
        }
        elCiclo += System.nanoTime() - startTimeFor;
        return count;
    }

    public boolean isGoalState() {
        return heuristic() == 0;
    }

    @Override
    public String toString() {
        StringBuilder boardPrint = new StringBuilder(size*size*3);
        for (int i=0;i<size;i++) {
            for (int j=0;j<size;j++) {
                boardPrint.append(board[i][j]);
                boardPrint.append(' ');
            }
            boardPrint.append('\n');
        }
        return boardPrint.toString();
    }
}
