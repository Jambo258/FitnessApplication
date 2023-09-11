import { Component } from '@angular/core';

@Component({
  selector: 'app-component',
  templateUrl: './component.component.html',
  styleUrls: ['./component.component.scss'],
})
export class ComponentComponent {
  board: string[] = Array(9).fill('');
  winner: string = '';
  currentPlayer: string = 'X';
  playerXmark: string = 'X';
  playerOMark: string = 'O';
  isGameOver: boolean = false;
  XScore: number = 0;
  OScore: number = 0;
  playerXName: string = 'Player 1';
  playerOName: string = 'Player 2';

  setPlayerNames() {
    const playerX = prompt('Enter Player 1 Name:');
    if (playerX) {
      this.playerXName = playerX;
    }

    const playerO = prompt('Enter Player 2 Name:');
    if (playerO) {
      this.playerOName = playerO;
    }
  }

  makeMove(index: number) {
    if (!this.isGameOver && this.board[index] === '') {
      this.board[index] = this.currentPlayer;
      if (this.checkWinner()) {
        this.winner = this.currentPlayer;
        this.isGameOver = true;
        if (this.winner === this.playerXmark) {
          this.XScore++;
          this.winner = this.playerXName;
        } else if (this.winner === this.playerOMark) {
          this.OScore++;
          this.winner = this.playerOName;
        }
      } else if (this.board.indexOf('') === -1) {
        this.isGameOver = true;
      } else {
        this.currentPlayer =
          this.currentPlayer === this.playerXmark
            ? this.playerOMark
            : this.playerXmark;
      }
    }
  }

  checkWinner(): boolean {
    const winPatterns: number[][] = [
      [0, 1, 2],
      [3, 4, 5],
      [6, 7, 8],
      [0, 3, 6],
      [1, 4, 7],
      [2, 5, 8],
      [0, 4, 8],
      [2, 4, 6],
    ];

    for (const pattern of winPatterns) {
      const [a, b, c] = pattern;
      if (
        this.board[a] &&
        this.board[a] === this.board[b] &&
        this.board[a] === this.board[c]
      ) {
        return true;
      }
    }

    return false;
  }

  restartGame() {
    this.currentPlayer = 'X';
    this.board = Array(9).fill('');
    this.winner = '';
    this.isGameOver = false;
  }
}

