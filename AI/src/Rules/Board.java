package Rules;

import java.util.LinkedList;
import Notation.Annotation;

public class Board {

    private char[][] chessBoard = new char[8][8];
    private final char[] initPosBlack = {'t', 'c', 'b', 'q', 'k', 'b', 'c', 't'};
    private final char[] initPosWhite = {'T', 'C', 'B', 'Q', 'K', 'B', 'C', 'T'};
    @SuppressWarnings("unchecked")
    private LinkedList<Coordinate>[][] stateBoard = new LinkedList[8][8];

    private boolean hasWhiteKingMoved = false;
    private boolean hasRightWhiteRookMoved = false;
    private boolean hasLeftWhiteRookMoved = false;
    private boolean hasBlackKingMoved = false;
    private boolean hasRightBlackRookMoved = false;
    private boolean hasLeftBlackRookMoved = false;

    public Board() {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                chessBoard[i][j] = 'o';
            }
        }
        for (int i = 0; i < chessBoard.length; i++)
            chessBoard[1][i] = 'p';
        for (int i = 0; i < chessBoard.length; i++)
            chessBoard[6][i] = 'P';
        for (int i = 0; i < chessBoard.length; i++)
            chessBoard[0][i] = initPosBlack[i];
        for (int i = 0; i < chessBoard.length; i++)
            chessBoard[7][i] = initPosWhite[i];
    }
    //comparação por tabela ASCII
    public boolean isWhite(int pos_i, int pos_j) throws BoardOutOfBoundsException {
    	return (Character.isUpperCase(getPiece(pos_i, pos_j)));
    }
    //comparação por tabela ASCII
    public boolean isBlack(int pos_i, int pos_j) throws BoardOutOfBoundsException {
    	return (Character.isLowerCase(getPiece(pos_i, pos_j)) && getPiece(pos_i, pos_j) != 'o');
    }
    public boolean isWhite(char piece) throws BoardOutOfBoundsException {
    	return (Character.isUpperCase(piece));
    }
    public boolean isBlack(char piece) throws BoardOutOfBoundsException {
    	return (Character.isUpperCase(piece) && piece != 'o');
    }
    public boolean hasSameColor(int me_i, int me_j, int that_piece_i, int that_piece_j) throws BoardOutOfBoundsException {
    	return ((isBlack(me_i, me_j) && isBlack(that_piece_i, that_piece_j))||(isWhite(me_i, me_j)&& isWhite(that_piece_i, that_piece_j)));
    }
    public void changePos(int begin_x, int begin_y, int final_x, int final_y){
        setChange(begin_x, begin_y, final_x, final_y);
        Annotation.putMovement(final_x, final_y);
    }
// Método de roque para as pretas. Pede um y para definir qual das duas possiblidades de roque vai ser feita.
    public void doBlacksCastling(int y) throws IllegalCastlingException, BoardOutOfBoundsException {
        if (y == 2 && getPiece(0, 2) == 'o' && !(getHasLeftBlackRookMoved()) && !(getHasBlackKingMoved())) {
            setChange(0, 4, 0, 2);
            setChange(0, 0, 0, 3);
        }
        else if (y == 6 && getPiece(0, 6) == 'o' && !(getHasRightBlackRookMoved()) && !(getHasBlackKingMoved())) {
            setChange(0, 4, 0, 6);
            setChange(0, 7, 0, 5);
        }
        else {
            throw new IllegalCastlingException("doBlacksCastling chamado ilegamente");
        }
    }
    public void doWhitesCastling(int y) throws IllegalCastlingException, BoardOutOfBoundsException {
        if (y == 2 && getPiece(7, 2) == 'o' && !(getHasLeftBlackRookMoved()) && !(getHasBlackKingMoved())) {
            setChange(7, 4, 7, 2);
            setChange(7, 0, 7, 3);
        }
        else if (y == 6 && getPiece(7, 6) == 'o' && !(getHasRightBlackRookMoved()) && !(getHasBlackKingMoved())) {
            setChange(7, 4, 7, 6);
            setChange(7, 7, 7, 5);
        }
        else {
            throw new IllegalCastlingException("doBlacksCastling chamado ilegamente");
        }
    }

    private void setChange(int begin_x, int begin_y, int final_x, int final_y){
        // a verificação é com a peça final? está peça de comparação é o rei?
       if(chessBoard[final_x][final_y] != 'K' && chessBoard[final_x][final_y] != 'k' && begin_x < 8 && begin_y < 8){
            chessBoard[final_x][final_y] = chessBoard[begin_x][begin_y];
            chessBoard[begin_x][begin_y] = 'o';
        }
        /*
        int a = (int)chessBoard[begin_x][begin_y];
        int b = (int)chessBoard[final_x][final_y];

        a+=b;
        b = a - b;
        a -= b;

        chessBoard[begin_x][begin_y] = (char)a;
        chessBoard[final_x][final_y] = (char)b;

        */
    }
    public char getPiece(int pos_x, int pos_y) throws BoardOutOfBoundsException {
        if (pos_x >= 8 || pos_y >= 8) {
            throw new BoardOutOfBoundsException("Board.getPiece tentou acessa uma casa além dos limites do tabuleiro.");
        }
        return chessBoard[pos_x][pos_y];
    }

    public LinkedList<Coordinate>[][] getStateBoard() {
        return stateBoard;
    }

    public void setStateBoard(LinkedList<Coordinate> moves, int i, int j) {
        stateBoard[i][j] = moves;
    }

    public void printImage(){
    	char[] letters = {'A','B','C','D','E', 'F', 'G', 'H'};
    	int[] numbers = {8,7,6,5,4,3,2,1}; 
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[i].length; j++) {
                System.out.print(chessBoard[i][j]);
            }
            System.out.print(" ");
            System.out.println(numbers[i]);
        }
        System.out.println();
        for (int i = 0; i < letters.length; i++)
			System.out.print(letters[i]);
    }
    public boolean getHasWhiteKingMoved(){
        return this.hasWhiteKingMoved;
    }
    public void setHasWhiteKingMovedAsTrue(){
        this.hasWhiteKingMoved = true;
    }
    public boolean getHasRightWhiteRookMoved(){
        return this.hasRightWhiteRookMoved;
    }
    public boolean getHasLeftWhiteRookMoved(){
        return this.hasLeftWhiteRookMoved;
    }
    public void setHasRightWhiteRookMovedAsTrue(){
        this.hasRightWhiteRookMoved = true;
    }
    public void setHasLeftWhiteRookMovedAsTrue(){
        this.hasLeftWhiteRookMoved = true;
    }
    public boolean getHasBlackKingMoved(){
        return this.hasBlackKingMoved;
    }
    public void setHasBlackKingMovedAsTrue(){
        this.hasBlackKingMoved = true;
    }
    public boolean getHasRightBlackRookMoved(){
        return this.hasRightBlackRookMoved;
    }
    public boolean getHasLeftBlackRookMoved(){
        return this.hasLeftBlackRookMoved;
    }
    public void setHasRightBlackRookMovedAsTrue(){
        this.hasRightBlackRookMoved = true;
    }
    public void setHasLeftBlackRookMovedAsTrue(){
        this.hasLeftBlackRookMoved = true;
    }

    public boolean hasPawnMoved(int pos_i, int pos_j) throws UnexpectedPieceException, BoardOutOfBoundsException {
        char piece = getPiece(pos_i, pos_j);
        if (piece != 'p' && piece != 'P') {
            throw new UnexpectedPieceException("Board.hasPawnMoved foi chamado em uma casa que não contém um peão");
        }
        else if (piece == 'p' && pos_i == 1) {
            return false;
        }
        else if (piece == 'P' && pos_i == 6) {
            return false;
        } else {
            return true;
        }
    }

    public void setBoard(char[][] board) {
        chessBoard = board;
    }

    public char[][] getBoard() {
        return chessBoard;
    }
    
    public Coordinate[] indexOfPiece(char b) {
    	Coordinate[] positions = new Coordinate[8];
    	int p = 0;
    	
    	for (int i = 0; i < positions.length; i++) 
    		positions[i] = null;
    	
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoard[i][j] == b) 
                	positions[p] = new Coordinate (i, j);
                	p++;
            }
        }
        return positions;
    }
    public boolean isAPiece(int pos_i, int  pos_j) throws BoardOutOfBoundsException {
    	return (getPiece(pos_i, pos_j) != 'o');
    }
    public byte[] HowManyKindOfAttacks(LinkedList<Coordinate> movements) throws BoardOutOfBoundsException {	
    	byte[] attacks = new byte[movements.size()/2];
    	for (int i = 0; i < movements.size(); i++) {
    		if(getPiece(movements.get(i).getPos_i(), movements.get(i).getPos_j()) == 'k' || getPiece(movements.get(i).getPos_i(), movements.get(i).getPos_j()) == 'K') {
    			attacks[i] = 2;
    		}
    		else {
    			attacks[i] = isAPiece(movements.get(i).getPos_i(), movements.get(i).getPos_j()) ? (byte)1 : (byte)0;
    		}
		}
    	
		return attacks;
    	
    }
    public boolean WillOccurCapture(LinkedList<Coordinate> movements) throws BoardOutOfBoundsException {
    	boolean r = false;
    	if(movements.equals(null))
    		return false;
    	for (byte element : HowManyKindOfAttacks(movements)) {
			if(element == (byte)1)
				r = true;
				break;
		}
    	
    	return r;
    }
    public boolean WillOccurCheck(LinkedList<Coordinate> movements) throws BoardOutOfBoundsException {
    	boolean r = false;
    	if(movements.equals(null))
    		return false;
    	for (byte element : HowManyKindOfAttacks(movements)) {
			if(element == (byte)2)
				r = true;
				break;
		}
    	
    	return r;
    }
 
	@SuppressWarnings("unchecked")
	public boolean WillOccurCheckMate(LinkedList<Coordinate> ... movements) throws BoardOutOfBoundsException {
    	boolean[] answers = new boolean[movements.length + 1];//todas as peças do cenário mais o rei.
    	for (int i = 0; i < movements.length; i++) {
    		for (byte element : HowManyKindOfAttacks(movements[i])) {
    			if(element == (byte)2)
    				answers[i] = true;
    				break;
    		}
    		if(answers[i] != true)
    			return false;
		}
    	char b;
    	//se a jogada forem das pretas... senão...
    	if(isBlack(movements[0].get(0).getPos_i(), movements[0].get(0).getPos_j())) 
    		b = 'K';
    	else
    		b = 'k';
    	
    	answers[answers.length - 1] = (Controller.getKingMoves(this, indexOfPiece(b)[0].getPos_i(), indexOfPiece(b)[0].getPos_j()) == null);
    	
		return answers[answers.length - 1];
    	
    	
    }
    //obs.: só chamar quando tiver certeza que pode ocorrer um xeque.
    public Coordinate WherePlayToCheck(LinkedList<Coordinate> movements) throws BoardOutOfBoundsException {
		for (int i = 0; i < HowManyKindOfAttacks(movements).length; i++) {
			if(HowManyKindOfAttacks(movements)[i] == 2) {
				return movements.get(i);
			}
		}
	
		return null;
    	
    }
    
    public boolean IsCheckMateInBlackKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1) throws BoardOutOfBoundsException {
    	return IsCheckMate(movements, movements1, 'k');
    }
    public boolean IsCheckMateInWhiteKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1) throws BoardOutOfBoundsException {
    	return IsCheckMate(movements, movements1,'K');
    }
    
    //algoritmo bruto
    private boolean IsCheckMate(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	/* * POSIÇÕES *
    	 * 
    	 * FRENTE
    	 * ATRÁS
    	 * ESQUERDA
    	 * DIREITA
    	 * DIAGONAL ESQUERDA SUPERIOR
    	 * DIAGONAL DIREITA SUPERIOR
    	 * DIAGONAL ESQUERDA INFERIOR
    	 * DIAGONAL DIREITA INFERIOR
    	 * 
    	 * */
    	
    	byte[] cases = new byte[8];
    	
    	cases[0] = pathForwardKing(movements, movements1, b);
    	cases[1] = pathBackwardKing(movements, movements1, b);
    	cases[2] = pathLeftKing(movements, movements1, b);
    	cases[3] = pathRightKing(movements,movements1, b);
    	cases[4] = pathTopLeftDiagonal(movements,movements1, b);
    	cases[5] = pathTopRightDiagonal(movements,movements1, b);
    	cases[6] = pathBottomLeftDiagonal(movements, movements1,b);
    	cases[7] = pathBottomRightDiagonal(movements,movements1, b);
    	
    	for (int i = 0; i < cases.length; i++) {
			if(cases[i] == (byte)1) {
				return false;
			}
		}
    	
    	return true;
    }
    
    // 0 -> borda; 1 -> tem uma peça da mesma cor; 2 -> tem uma peça oponente  
    
    private byte pathForwardKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i >=0 ; i--) {
			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(i, coordinate.getPos_j()) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, coordinate.getPos_j()) && 
					(getPiece(i, coordinate.getPos_j()) == 'T'||getPiece(i, coordinate.getPos_j()) == 't'
					||getPiece(i, coordinate.getPos_j()) == 'q'||getPiece(i, coordinate.getPos_j()) == 'Q'))) {
				return 2;
			}else if(getPiece(i, coordinate.getPos_j()) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, coordinate.getPos_j())) {
				return 1;
			}
		}
    	
    	return 0;
    }
    private byte pathBackwardKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i < 8 ; i++) {
			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(i, coordinate.getPos_j()) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, coordinate.getPos_j()) && 
					(getPiece(i, coordinate.getPos_j()) == 'T'||getPiece(i, coordinate.getPos_j()) == 't'
					||getPiece(i, coordinate.getPos_j()) == 'q'||getPiece(i, coordinate.getPos_j()) == 'Q'))) {
				return 2;
			}else if(getPiece(i, coordinate.getPos_j()) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, coordinate.getPos_j())) {
				return 1;
			}
		}
    	
    	return 0;
    }
    
    private byte pathLeftKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int j = coordinate.getPos_j(); j >=0 ; j--) {
			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(coordinate.getPos_i(), j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), coordinate.getPos_i(), j) && 
					(getPiece(coordinate.getPos_i(), j) == 'T'||getPiece(coordinate.getPos_i(), j) == 't'
					||getPiece(coordinate.getPos_i(), j) == 'q'||getPiece(coordinate.getPos_i(), j) == 'Q'))) {
				return 2;
			}else if(getPiece(coordinate.getPos_i(), j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), coordinate.getPos_i(), j)) {
				return 1;
			}
		}
    	
    	return 0;
    }
    private byte pathRightKing(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int j = coordinate.getPos_j(); j < 8 ; j++) {
			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(coordinate.getPos_i(), j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), coordinate.getPos_i(), j) && 
					(getPiece(coordinate.getPos_i(), j) == 'T'||getPiece(coordinate.getPos_i(), j) == 't'
					||getPiece(coordinate.getPos_i(), j) == 'q'||getPiece(coordinate.getPos_i(), j) == 'Q'))) {
				return 2;
			}else if(getPiece(coordinate.getPos_i(), j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), coordinate.getPos_i(), j)) {
				return 1;
			}
		}
    	
    	return 0;
    }
    private byte pathTopLeftDiagonal(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i >=0 ; i--) {
    		for (int j = coordinate.getPos_j(); j >= 0 ; j--) {
    			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(i, j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(),i, j) && 
    					(getPiece(i, j) == 'B'||getPiece(i, j) == 'b'
    					||getPiece(i, j) == 'q'||getPiece(i, j) == 'Q'||getPiece(i, j) == 'P'||getPiece(i, j) == 'p'))) {
    				return 2;
    			}else if(getPiece(i, j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, j)) {
    				return 1;
    			}
    		}
		}
    	
    	return 0;
    }    
    private byte pathTopRightDiagonal(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i >=0 ; i--) {
    		for (int j = coordinate.getPos_j(); j < 8 ; j++) {
    			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(i, j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(),i, j) && 
    					(getPiece(i, j) == 'B'||getPiece(i, j) == 'b'
    					||getPiece(i, j) == 'q'||getPiece(i, j) == 'Q'||getPiece(i, j) == 'P'||getPiece(i, j) == 'p'))) {
    				return 2;
    			}else if(getPiece(i, j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, j)) {
    				return 1;
    			}
    		}
		}
    	
    	return 0;
    }
    private byte pathBottomLeftDiagonal(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i < 8 ; i++) {
    		for (int j = coordinate.getPos_j(); j >= 0 ; j--) {
    			if(WillOccurCheck(movements) || WillOccurCheck(movements1) ||(getPiece(i, j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(),i, j) && 
    					(getPiece(i, j) == 'B'||getPiece(i, j) == 'b'
    					||getPiece(i, j) == 'q'||getPiece(i, j) == 'Q'||getPiece(i, j) == 'P'||getPiece(i, j) == 'p'))) {
    				return 2;
    			}else if(getPiece(i, j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, j)) {
    				return 1;
    			}
    		}
		}
    	
    	return 0;
    }
    private byte pathBottomRightDiagonal(LinkedList<Coordinate> movements, LinkedList<Coordinate> movements1, char b) throws BoardOutOfBoundsException {
    	
    	Coordinate coordinate = indexOfPiece(b)[0];
    	
    	for (int i = coordinate.getPos_i(); i < 8 ; i++) {
    		for (int j = coordinate.getPos_j(); j < 8 ; j++) {
    			if(WillOccurCheck(movements) || WillOccurCheck(movements1) || (getPiece(i, j) != 'o' && !hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(),i, j) && 
    					(getPiece(i, j) == 'B'||getPiece(i, j) == 'b'
    					||getPiece(i, j) == 'q'||getPiece(i, j) == 'Q'||getPiece(i, j) == 'P'||getPiece(i, j) == 'p'))) {
    				return 2;
    			}else if(getPiece(i, j) != 'o' && hasSameColor(coordinate.getPos_i(), coordinate.getPos_j(), i, j)) {
    				return 1;
    			}
    		}
		}
    	
    	return 0;
    }
    /*
    public boolean isPiecePinned(int i, int j) throws BoardOutOfBoundsException, UnexpectedPieceException {
        if (getPiece(i, j) != 'o' && isWhite(i, j) == true) {
            boolean kingPath = false;
            LinkedList<Coordinate> queen = Controller.getQueenMoves(this, i, j);
            Iterator<Coordinate> it = queen.iterator();
            Coordinate x;

            while (it.hasNext()) {
                x = it.next();
                
                 if (getPiece(x.getPos_i(), x.getPos_j()) == 'K') {

                 }

                 else {
                    continue;
                 }
            }
            
            
        }

        if (getPiece(i, j) != 'o' && isBlack(i, j) == true) {
            
            
            return true;
        }

        else {
            
            throw new UnexpectedPieceException("Casa Vazia");
        }
    }*/
}