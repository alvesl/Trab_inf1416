Trab_inf1416
============

Colaboradores:
Carlo Melo Caratori
Lucas de Queiroz Alves

Trabalho de INF-1416

------ TODO ------

- Insercao de Usuario
	- OK Inserir novo usuario
	- OK Inserir senha corretamente = HEX(HASH_MD5(password + ID + username))
	- Inserir PublicKey corretamente = caminho da chave publica OU HEX(publicKey)
	- OK Inserir o numero de acessos de um determinado usuario (apos 3 acessos senha tem que ser redefinida)
	
- Criacao de senha
	- Verificar tamanho da senha (min 8, max 10)
	- Verificar forca da senha (nao aceitar sequencias de repeticoes de digitos, nem digitos sequenciais crescentes ou decrescentes)
	
- GUI
	- OK Tela inicial (login)
	- OK Tela senha
	- Tela Principal
	- Tela Cadastro novo Usuário
	- Tela Alteracao Usuario
	- Tela Consultar pasta de arquivos secretos de um usuario
	