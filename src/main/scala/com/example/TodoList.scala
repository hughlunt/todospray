package com.example
import spray.json._

/**
  * Created by hlunt on 18/10/16.
  */
object TodoList {
  var todos: List[Todo] = List()

  def findTodoById(id: Int): Todo = {
    todos.filter(t => t.id == id).head
  }

  def delete(id: Int): Unit = {
    todos = todos.filter(t => t.id != id)
  }

  def add(todo: Todo): Unit = {
    todos = todos :+ todo
  }

  def update(id: Int, todoUpdate: TodoUpdate) = {
    val old: Todo = findTodoById(id)
    delete(id)
    add(Todo(id,
      todoUpdate.title.getOrElse(old.title),
      todoUpdate.completed.getOrElse(old.completed),
      old.url,
      todoUpdate.order.getOrElse(old.id))
    )
  }

  def clear() {
    todos = List[Todo]()
  }

  def getNextId: Int = {
    todos.size match {
      case 0 => 1
      case _ => todos.maxBy(_.id).id  + 1
    }
  }

  def print(x: Todo) = {
    x.toJson.prettyPrint
  }
  def print(x: List[Todo]) = {
    x.toJson.prettyPrint
  }
}
