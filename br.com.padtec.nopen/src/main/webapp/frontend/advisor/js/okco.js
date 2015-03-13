/*
* Author: Freddy Brasileiro
*/
//Function loading
function loading()
{
 	var maskHeight = $(document).height();
	var maskWidth = "100%";//$(document).width();

	//Define largura e altura do div#maskforloading iguais ás dimensões da tela
	$('#maskforloading').css({'width':maskWidth,'height':maskHeight});

	//efeito de transição
	$('#maskforloading').show();
}