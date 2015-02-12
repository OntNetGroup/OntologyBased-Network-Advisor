// Function loading
function loading()
{
	document.body.style.cursor='wait';
	var maskHeight = $(document).height();
	//Define largura e altura do div#maskforloading iguais �s dimens�es da tela
	$('#maskforloading').css({'height':maskHeight});
	
	//efeito de transi��o
	$('#maskforloading').show();
}