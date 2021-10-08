# Todo App
> Um aplicativo de organização de tarefas para Android
![ping](./app/src/main/res/mipmap-xxxhdpi/custom_launcher_round.png)


O aplicativo Todo tem como objetivo tornar a vida do usuário mais organizada proporcionando a ele um aplicativo de fácil visualização 
mas ao mesmo tempo com várias features que permitem ao usuário definir tarefas de forma simples e intuitiva. 
Além disso possui um design moderno  para tornar o uso do app mais confortável para o usuário.

## Instalação

Android:

> A instalação se passa em um ambiente Desktop

1. Instale o programa Android Studio;
2. Crie um projeto a partir do repositório;


![Selecione a opção de criação pelo git](./img/android_studio_get_from_vc.png)


![Preencha com o url do repo e o diretório](./img/android_studio_get_from_vc_2.png)


3. Conecte o seu dispositivo Android a seu Desktop;
3.1 Permita a conexão entre os dispositivos no seu Android;
4. Selecione o seu dispositivo para o aplicativo executar;

![Selecione o dispositivo](./img/dispositivos.png)

5. Clique em executar e espere o AndroidStudio relatar sucesso na execução;
6. Pronto, o aplicativo está instalado.

## Exemplo de uso

- Crie uma tarefa

![Create Note](./img/createNote.gif)

- Crie tarefa a partir de texto selecionado e visualize

![Selected](./img/Selcted.gif)

## Histórico de lançamentos

* 1.0.0
    * O primeiro lançamento adequado
* 0.9.1
    * MUDANÇA: Atualização de docs (código do módulo permanece inalterado)
* 0.7.0
    * Consertado: Conserto na criação da nota
* 0.5.1
    * CONSERTADO: Crash quando chama `list()` 
* 0.1.0
    * Branch `app` criada focada no aplicativo
    * Branch `database` criada focada no Room database
* 0.0.1
    * Trabalho em andamento

## Contributing

1. Faça o _fork_ do projeto (<https://github.com/ivarejao/Todo.git>)
2. Crie uma _branch_ para sua modificação (`git checkout -b feature/fooBar`)
3. Faça o _commit_ (`git commit -am 'Add some fooBar'`)
4. _Push_ (`git push origin feature/fooBar`)
5. Crie um novo _Pull Request_
