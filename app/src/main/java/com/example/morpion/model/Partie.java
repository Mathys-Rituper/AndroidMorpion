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
        //Return codes : 0 = not over, 1 = current player a gagné, 2 = c'est plein, 3 = erreur
        if (c < grid.size() * grid.size() && grid.get(c) == 0){

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
    } else return 3;
    }


    public void nextPlayer(){
        if (currentPlayer==player1){
            currentPlayer=player2;
        } else {
            currentPlayer = player1;
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
            if (!grid.get(i).equals((Integer) player_int)){
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
            if (!grid.get(i).equals((Integer) player_int)){
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
        boolean topleft = true;
        for (int i = 0; i<taille *taille; i = i + (taille + 1) ){
            if (!grid.get(i).equals((Integer) player_int)){
                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                    topleft = false;
            }
        }

        //diagonale du haut à droite vers le bas à gauche
        boolean topright = true;
        for (int i = taille - 1; i<taille *taille-1; i = i + ((taille -1 ))){
            if (!grid.get(i).equals((Integer) player_int)){

                // si une des cases de la ligne n'est pas détenue par le joueur courant, il n'a pas gagné
                topright=false;
            }
        }
        return topleft || topright;
    }

    private boolean isFull(){
        for (int i = 0; i<taille * taille; i++){
            if (grid.get(i) ==0) return false;
        }
        return true;
    }

    private void finPartieNonNulle(User winner){

        this.winner = winner;
        winner.setVictories(winner.getVictories()+1);
        if (this.winner.equals(player1)) {
            player2.setDefeats(player2.getDefeats()+1);
        } else {
            player1.setDefeats(player1.getDefeats()+1);
        }

    }

    private void finPartieNulle() {
        winner=null;
        this.player1.setTies(this.player1.getTies()+1);
        this.player2.setTies(this.player2.getTies()+1);
    }


    public User getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(User currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public User getWinner() {
        return winner;
    }

    public void setWinner(User winner) {
        this.winner = winner;
    }

    public int getTaille() {
        return taille;
    }

    public ArrayList<Integer> getGrid() {
        return grid;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }
}
