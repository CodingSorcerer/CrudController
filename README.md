# CrudController
A generic controller for Crud operations to be used in spring boot projects

To use this controller, simply import it into your project. Once it's imported annotate the repositories that you want to use for simple crud APIs with @CrudRepo and fill in the entity class and path name. it will automatically include get, post and delete functionality.

entityClass = the class of the entity that the repository is responsible for.
pathName = The base path you want your controller to have. example: Http://SomeServer:aport/<pathName>

There are also various booleans you can set in @CrudRepo to alter the actions that are allowed to be performed using the annotated repository.


Example project using CrudController library: https://github.com/CodingSorcerer/Spring-JPA-Example
