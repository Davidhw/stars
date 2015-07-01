// handels displaying the board and dealing with clicks

// the canvas is hardcoded at 320x320
// this has only been tested for a board width and height of 16
var vcH = canvasHeight/board.height;
var vcW = canvasWidth/board.width;

drawBoard();
document.onclick = clickHandler;


// if the user clicks somewhere in the canvas: move to that location
// otherwise, ignore them
function clickHandler(e){
	// get the canvas created in main
	c = document.getElementById("myCanvas");
	
	// if the click is outside the canvas, ignore it
	if (e.pageX>(c.offsetWidth-c.offsetLeft)||e.pageX<c.offsetLeft||e.pageY>(c.offsetTop+c.offsetHeight)||e.pageY<c.offsetBottom){
		console.log("Click outside canvas");
		console.log(e.pageX);
		console.log(e.pageY);
		return;
	// otherwise, check if you're allowed to move
	}else{
		// you're not allowed to move if the game is over
		if (board.lost||board.won){
			console.log("You cant move, the game is over.");
			}else{
				// if you can move, convert the coordinates to a grid location
				var x = Math.floor((e.pageX-c.offsetLeft)/vcW);
				var y = Math.floor((e.pageY-c.offsetTop)/vcH);
				
				// send a post request to get a board with with the given 
				// location explored
				$.post("/move", {board: JSON.stringify(board),x:x,y:y}, function(response){
					  board = JSON.parse(response);
					  
					  // draw the new board
					  drawBoard();
					  
					  // alert the new player of they won or lost
					  if (board.won){
						alert("You win!");
						}
					  if (board.lost){
						alert("You lose!");
						}
					  return;
				});
			}
	}
}

// draws the minesweeper grid and the tiles in it
function drawBoard(){
	var c = document.getElementById("myCanvas");
	var ctx = c.getContext("2d");

	// clear the canvas
	ctx.clearRect(0, 0, c.width, c.height)
		
	// draw the tiles on the grid and write their labels
	for (i = 0; i< board.width; i++){	
		for (j = 0; j< board.height; j++){
			if (board.grid[i][j].visited){
				ctx.fillStyle = "#dddddd";
				ctx.fillRect(i*vcW,j*vcH,vcW,vcH);
				ctx.fillStyle = "#000000";
				ctx.font = "22px Arial"
				// not sure why i have to increase the column val by one
				ctx.fillText(board.grid[i][j].label,i*vcW,(j+1)*vcH);
				} 
			}
		}	
		
	// draw the horizontal lines
	for (i = 0; i< board.width; i++){
		ctx.beginPath();
		ctx.moveTo(vcW*i,0);
		ctx.lineTo(vcW*i,board.width*vcW);	
		ctx.stroke();
			
		}
		
	// draw the vertical lines
	for (j = 0; j< board.height; j++){
		ctx.beginPath();
		ctx.moveTo(0,vcH*j);
		ctx.lineTo(vcH*board.height,vcH*j);
		ctx.stroke();
		}
		

		
}	