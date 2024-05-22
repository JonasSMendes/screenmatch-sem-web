
# Desafio back-end java/Spring catalogo de series

![image](https://github.com/JonasSMendes/screenmatch-web/assets/119429346/fd403947-b523-4968-8ca2-777d92612d9e)


## catologo de series

* API documentação - [OmdApi](https://www.omdbapi.com/)

### Usando Banco de dados postgresql

- Documentação [postgresql](https://www.postgresql.org/docs/)

- projeto [MVC](https://www.alura.com.br/apostila-java-web/mvc-model-view-controller)

- ![image](https://github.com/JonasSMendes/screenmatch-web/assets/119429346/974c0601-a1ff-4821-906f-67505da1e322)

- Front-end do desafio [Alura](https://github.com/jacqueline-oliveira/3356-java-web-front)


### Aplicabilidade do banco de dados

Como o desafio foi feito em postgress, a pasta application.properties precisa ser preenchida com os seus dados do banco:

* spring.datasource.url=jdbc:postgresql://${DB_HOST -- seu localhost}/livraria
* spring.datasource.username=${DB_USERNAME -- nome}
* spring.datasource.password=${DB_PASS -- senha}
* spring.datasource.driver-class-name=org.postgresql.Driver
* hibernate.dialect=org.hibernate.dialect.HSQLDialect

### Como adicionar novos titulos?

Para buscar novos titulos basta descomentar a classe  **ScreenmatchApplicationSemWeb** 
e comentar a classe **ScreenmatchApplication**

![image](https://github.com/JonasSMendes/screenmatch-web/assets/119429346/82d864a3-8df8-4b1e-bc59-174f00f53445)

Ao inicializar o programa terá uma lista de opções:

![image](https://github.com/JonasSMendes/screenmatch-web/assets/119429346/13a0a614-646b-469d-8b6d-3e09578290ac)

A serie sera guardada no seu banco de dados.
Ao inicializar novamente o programa agora com o **ScreenmatchApplication** descomentado, sua serie estará na aplicação.
**lembre de comentar novamente a classe ScreenmatchApplicationSemWeb para rodar o programa no site**
