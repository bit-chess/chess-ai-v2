package Algorithm;

import Rules.Bits;
import Rules.Manipulator;
import Rules.Movements;

public class Evaluate {
    Bits bit;
    Movements move;
    long attackWhite;
    long attackBlack;

    public Evaluate(Bits bit) {
        this.bit = bit;
        this.move = new Movements(bit);
        this.attackWhite = move.whiteAttackMap();
        this.attackBlack= move.blackAttackMap();
    }
    //pretas pares, brancas ímpares
    // peão 0 e 1; cavalo: 2 e 3; bispo: 4 e 5; torre: 6 e 7; rainha: 8 e 9; rei: 10 e 11
    private double piece() {
        double white = 0;
        double black = 0;
      
        for (int i = 0; i < 64; i++) {
            if (Manipulator.isWhite(i, bit)) {
                if (Manipulator.getPiece(i, bit) == 'P')
                    white = white + 1;
                if (Manipulator.getPiece(i, bit) == 'Q')
                    white = white + 9;
                if (Manipulator.getPiece(i, bit) == 'T')
                    white = white + 5;
                if (Manipulator.getPiece(i, bit) == 'B')
                    white = white + 3.5;
                if (Manipulator.getPiece(i, bit) == 'C')
                    white = white + 3;
            } else if (Manipulator.isBlack(i, bit)) {
                if (Manipulator.getPiece(i, bit) == 'p')
                    black = black + 1;
                if (Manipulator.getPiece(i, bit) == 'q')
                    black = black + 9;
                if (Manipulator.getPiece(i, bit) == 't')
                    black = black + 5;
                if (Manipulator.getPiece(i, bit) == 'b')
                    black = black + 3.5;
                if (Manipulator.getPiece(i, bit) == 'c')
                    black = black + 3;
            }
        }

        return (white - black);
    }

    private double kingSafety()  {
        double white = 0;
        double black = 0;
        int w = Manipulator.squareOfPiece('K', bit);
        int b = Manipulator.squareOfPiece('k', bit);

        long x = move.queenMoves(w)& ~bit.white;
        white = -(Math.sqrt((double)Long.bitCount(x))/1.5);

            
        long y = move.queenMoves(b)& ~bit.black;
        black = -(Math.sqrt((double)Long.bitCount(y))/1.5);
            
        
        return (white - black);
    }

    private double pieceSafety(){
        double white= 0;
        double black = 0;
        for(int mid = 0; mid < 64; mid++){
            if (Manipulator.isBlack(mid, bit) && move.isDefendedBlack(mid)) {
                if ((Manipulator.getPiece(mid, bit) == 'b') || (Manipulator.getPiece(mid, bit) == 't') || (Manipulator.getPiece(mid, bit) == 'c')) {
                    black+=0.2;
                }
                if ((Manipulator.getPiece(mid, bit) == 'p')) {
                    black += 0.05;
                }
            }
            else if (Manipulator.isWhite(mid, bit) && move.isDefendedWhite(mid)) {
                if (Manipulator.getPiece(mid, bit) == 'B' || (Manipulator.getPiece(mid, bit) == 'T') || (Manipulator.getPiece(mid, bit) == 'C')) {
                    white+=0.2;
                }
                if ((Manipulator.getPiece(mid, bit) == 'P')) {
                    white += 0.05;
                }
            }
        }
        return (white - black);

    }
    private double pawnAdvancement() {
        double white = 0;
        double black = 0;
        long whitePawn = Manipulator.indexOfPiece('P', bit);
        long blackPawn = Manipulator.indexOfPiece('p', bit);
        while (whitePawn != 0L) {
            long x = Manipulator.lsb(whitePawn);
            int pos = Manipulator.positionOfBit(x);
            white += (6 - (pos/8))*0.1;
            whitePawn = Manipulator.reset(whitePawn);
        }
        while (blackPawn != 0L) {
            long x = Manipulator.lsb(blackPawn);
            int pos = Manipulator.positionOfBit(x);
            black += ((pos/8) - 1)*0.1;
            blackPawn = Manipulator.reset(blackPawn);
        }

        
        return (white - black);

        
    }

    private double pieceMobility() {
        double white = 0;
        double black = 0;
        for(int mid = 0; mid < 64; mid++){
            if (Manipulator.getPiece(mid, bit) != 'q' || Manipulator.getPiece(mid, bit) != 'Q') {
                long x = move.getPieceMove(mid) & ~bit.board;
                if (Manipulator.isWhite(mid, bit)) {
                    white += Math.sqrt((double)Long.bitCount(x));
                }
                else if(Manipulator.isBlack(mid, bit)) {
                    black += Math.sqrt((double)Long.bitCount(x));
                }                                
            } 
        }
        return (white-black);
    }
   
    public double total () {
        return (pieceSafety() + piece() + kingSafety() + pawnAdvancement() + pieceMobility());
    }
    
}
