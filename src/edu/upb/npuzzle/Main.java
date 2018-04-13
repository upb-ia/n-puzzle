package edu.upb.npuzzle;

public class Main {

    public static void main(String[] args) {
        PuzzleState puzzle = new PuzzleState( 3 );
        System.out.println(puzzle.toString());
        Searcher search = new Searcher( puzzle );
        System.out.println(search.findSolution());
    }
}
