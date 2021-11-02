package com.example.morpion.model;

import java.util.ArrayList;
import java.util.List;

public class Partie {

    private final int taille;
    private ArrayList<Integer> grid;
    private final User player1;
    private final User player2;
    private User currentPlayer;
    private User winner;

    public Partie( int taille , User player1, User player2) {
        assert taille > 0;
        this.taille = taille;
        this.player1 = player1;
        this.player2 = player2;
        this.grid= new ArrayList<Integer>();
        for (int i = 0; i<taille*taille; i++) {
            grid.add(0);
        }
        currentPlayer = player1;
        winner = null;
    }

    public int jouerCoup(Integer c) {
        //Return codes : 0 = not over, 1 = current player a gagné, 2 = c'est plein
        if (this.currentPlayer == this.player1) {
            this.grid.set(c, 1);
        } else {
            this.grid.set(c, 2);
        }
        if (checkLigneForWin(c) || checkColumnForWin(c) || checkDiagonals()) {
            finPartieNonNulle(currentPlayer);
            return 1;
        } else if (isFull()) {
            finPartieNulle();
            return 2;
        } else return 0;
    }

    public void nextPlayer(){
        if (this.currentPlayer==this.player1){
            this.currentPlayer=this.player2;
        } else {
            this.currentPlayer = this.player1;
        }
    }

    private boolean checkLigneForWin(Integer c){
        int firstIndexOfLine =c - (c%(int)this.taille);
        int player_int;
        if (currentPlayer == player1){
            player_int= 1;
        } else {
            player_int = 2;
        }

        for (int i = firstIndexOfLine;i<firstIndexOfLine+this.taille;i++){
            if (grid.get(i)!=(Integer) player_int){
                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                return false;
            }
        }

        //si toutes les cases de la ligne sont détenues par le joueur courant, il a gagné
        return true;
    }

    private boolean checkColumnForWin(Integer c){
        int firstIndexOfColumn = c % taille;
        int player_int;
        if (currentPlayer == player1){
            player_int= 1;
        } else {
            player_int = 2;
        }

        for (int i = firstIndexOfColumn;i<taille * taille;i = i+ taille){
            if (grid.get(i)!=(Integer) player_int){
                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                return false;
            }
        }
        //si toutes les cases de la ligne sont détenues par le joueur courant, il a gagné
        return true;
    }

    private boolean checkDiagonals(){
        int player_int;
        if (currentPlayer == player1){
            player_int= 1;
        } else {
            player_int = 2;
        }

        //diagonale du haut à gauche vers le bas à droite
        for (int i = 0; i<taille *taille; i = i + ((taille + 1))){
            if (grid.get(i)!=(Integer) player_int){
                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                return false;
            }
        }

        //diagonale du haut à droite vers le bas à gauche
        for (int i = taille - 1; i<taille *taille; i = i + ((taille -1 ))){
            if (grid.get(i)!=(Integer) player_int){
                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                return false;
            }
        }
        return true;
    }

    private boolean isFull(){
        for (int i = 0; i<taille * taille; i++){
            if (i ==0) return false;
        }
        return true;
    }

    private void finPartieNonNulle(User winner){
        this.winner = winner;
        winner.setVictories(winner.getVictories()+1);
        if (winner == player1) {
            player2.setDefeats(player2.getDefeats()+1);
        } else {
            player1.setDefeats(player1.getDefeats()+1);
        }
    }

    private void finPartieNulle() {
        this.player1.setTies(this.player1.getTies()+1);
        this.player2.setTies(this.player2.getTies()+1);
    }


}
