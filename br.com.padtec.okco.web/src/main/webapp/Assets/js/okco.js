// Function loading
function loading()
{
	document.body.style.cursor='wait';
	var maskHeight = $(document).height();
	//Define largura e altura do div#maskforloading iguais ás dimensões da tela
	$('#maskforloading').css({'height':maskHeight});
	
	//efeito de transição
	$('#maskforloading').show();
}