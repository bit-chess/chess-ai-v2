#define len 8
#define GREEN_LED 12
#define RED_LED 13
typedef struct Pair{
   int x, y;
}pair;

typedef struct Board{
  const int board[len][len] ={};//Matriz com os pinos do tabuleiro
                             
  int stateBoard[len][len]; //Matriz com o estado de cada pino 
  int lastState[len][len]; 
  
  pair lastMovement;//pair com o último movimento
  pair nowMovement;//pair com a jogada realizada
 
  bool turn = true;
  
  void updateBoard(){
    for(int i=0;i<len;i++)
      for(int j=0;j<len;j++)
        stateBoard[i][j] = digitalRead(board[i][j]);
  }
  
  void init(){
    for(int i=0;i<len;i++)
      for(int j=0;j<len;j++)
        pinMode(board[i][j], INPUT);
    updateBoard();
    Serial.begin(9600);
    pinMode(GREEN_LED, OUTPUT);
    pinMode(RED_LED, OUTPUT);
  }
  
  bool capture(){   
    for(int i=0;i<len;i++)
      for(int j=0;j<len;j++)
        lastState[i][j] = stateBoard[i][j];
      
    updateBoard();
    int countLast = 0;
    int countState = 0;
    for (int i = 0; i<len; i++) for(int j=0;j<len;j++){
        countLast+=(int)lastState[i][j];
        countState+=(int)stateBoard[i][j];
    }
    if (countState<countLast) return true;
    return false;
  }

  bool movement(){
    for(int i=0;i<len;i++)
      for(int j=0;j<len;j++)
        lastState[i][j] = stateBoard[i][j];
      
    updateBoard();
    bool cond = false;
    int i = 0;
    int j = 0;  
    for(i=0;i<len;i++) {
      for(j=0;j<len;j++){
        if(lastState[i][j] == HIGH && lastState[i][j] != stateBoard[i][j]){
          cond = true;
          break;
        }
      }
      if(cond) break;
    }
      while(cond){
        updateBoard();
        for (int m=0;m<len;m++) {
          if(m==i) continue;
          for(int n=0;n<len;n++){
            if(n==j) continue;
            if (lastState[m][n]==LOW && lastState[m][n] != stateBoard[m][n]){
              lastMovement.x = i;
              lastMovement.y = j;
              nowMovement.x = m;
              nowMovement.y = n;
              turn = !turn;
              return true;
            }
            if (lastState[m][n]==HIGH && lastState[m][n] != stateBoard[m][n]){
              nowMovement.x = i;
              nowMovement.y = j;
              lastMovement.x = m;
              lastMovement.y = n;
              turn = !turn;
              return true;
            }
          }
        }
      }  
      turn = !turn;
    return false;
  }
  
  void printStateBoard(){
    for(int i=0;i<len;i++){ 
       for(int j=0;j<len;j++){
        Serial.print(stateBoard[i][j]);
            Serial.print(" ");
      }
      Serial.println();
    }
    Serial.println();    
  }
  void clearState(){
    lastMovement.x = -1;
    lastMovement.y = -1;
    nowMovement.x = -1;
    nowMovement.y = -1;
  }
  bool backGame(){
    for(int i=0;i<len;i++) for(int j=0;j<len;j++){
      if(lastState[i][j]!=stateBoard[i][j]) return false; 
    }        
    return true;
  }
  
}tab;

tab chess;

void setup(){
  chess.init();
}
void loop(){
  chess.updateBoard(); 
  chess.printStateBoard();

  if(chess.turn){
    //movimento das brancas
    while(chess.turn){
      //mover
      chess.movement(); 
    }
  }
  if(!chess.turn) {
    //movimento das pretas
    //comunicação serial é aqui
    byte in = Serial.read();
    if(in == 0){//Se as brancas realizaram mov ilegal
      while(!chess.backGame()){
        digitalWrite(GREEN_LED, LOW);
        digitalWrite(RED_LED, HIGH);
      }
      digitalWrite(GREEN_LED, HIGH);
      digitalWrite(RED_LED, LOW);
      chess.turn = true;
      
    }else if(in == 1){
      //Realiza o mov do robo
    }
  }
  
  delay(5);
}
