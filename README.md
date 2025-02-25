# Book-Stock-System

Funcionalidades:

Admin
Pegar todos os admins
Remover um admin
Cadastrar um admin

Login
Registro apenas pelo administrador, pode adicionar um novo administrador
Remover usuário administrador -> não é possível remover se só existir um usuário

Livros
Pegar todos os livros
Ver detalhes de um livro ao clicar nele
Adicionar livro -> adicionar por completo
Adicionar quantidade a um livro existente -> end point enviando o id do livro e incrementa
Remover quantidade de um livro existente -> se ficar com quantidade = 0 não remove do banco de dados, aparece uma tarja de esgotado
Editar as informações do livro

GET /api/book
GET /api/book/{id}
POST /api/book
PATCH /api/book/{id}
DELETE /api/book/{id}
PATCH /api/book/{id}

GET /api/admin
DELETE /api/admin/{id}
POST /api/admin/register
POST /api/admin/login