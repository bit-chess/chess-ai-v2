package Analyzer;

import Notation.Translator;
public class Movement {
    private int x0, xF, y0, yF, sequential_flag;
    private String movement_;

    private ImageBoard initial_image, final_image;
    public Movement(){
        sequential_flag = 0;

        x0 = -1;
        xF = -1;
        y0 = -1;
        yF = -1;

        movement_ = "XXXX";

        initial_image = new ImageBoard();
        final_image = new ImageBoard();
    }

    int counterPiece(ImageBoard board){
        int acc = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) if(board.board[i][j] != 'x'){
                acc++;
            }
        }
        return acc;
    }

    boolean analyzer(){
        int[][] analyser = new int[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                analyser[i][j] = initial_image.board[i][j] ^ final_image.board[i][j];
            }
        }
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(analyser[i][j] > 0 && initial_image.board[i][j] != 'x'){
                    x0 = i;
                    y0 = j;
                }
                else if(analyser[i][j] > 0 && final_image.board[i][j] != 'x'){
                    xF = i;
                    yF = j;
                }
            }
        }


        if(x0 == xF && y0 == yF && (x0 != -1 || y0 != -1 || xF != -1 || yF != -1)) {
            return false;
        }
        //sprintf(ans.movement_, "%c%d%c%d", get_letter(ans.y0), number_letter(ans.x0), get_letter(ans.yF), number_letter(ans.xF));
        movement_ = Translator.NotationComputerToChess(x0, y0);
        movement_ += Translator.NotationComputerToChess(xF, yF);

        return true;
    }

    void find_movement_FSM(ImageBoard curr_board, int how_many_piece){
        switch (sequential_flag)
        {
            case 0:
                if(how_many_piece - 1 == counterPiece(curr_board)) sequential_flag = 1;
                else initial_image = curr_board;
                break;

            case 1:
                if(how_many_piece == counterPiece(curr_board)) sequential_flag = 2;
                else if(how_many_piece - 2 == counterPiece(curr_board)) sequential_flag = 3;
                break;

            case 2:
                final_image = curr_board;
                if(analyzer()) sequential_flag = 0;
                break;

            case 3:
                if(how_many_piece - 1 == counterPiece(curr_board)) sequential_flag = 2;
                break;

        }
    }
    SetMovement getAnswer(){
        return new SetMovement(x0, xF, y0, yF, movement_);
    }

}
