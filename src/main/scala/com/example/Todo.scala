package com.example

import spray.json.DefaultJsonProtocol

case class Todo (id: Int, title: String, completed: Boolean, url: String, order: Int)

object Todo extends DefaultJsonProtocol {
  implicit val todoFormat = jsonFormat5(Todo.apply)
}

case class TodoUpdate(title: Option[String], completed: Option[Boolean], order: Option[Int])

object TodoUpdate extends DefaultJsonProtocol {
  implicit val todoBuilderFormat = jsonFormat3(TodoUpdate.apply)
}

case class TodoCreator(title: String, order: Option[Int]) {
  def create(id: Int, title: String, order: Int): Todo = {
    def buildUrl(id: Int): String = {
      "http://localhost:8080/todo/" + id.toString
    }
    Todo(id, title, completed = false, buildUrl(id), order)
  }
}

object TodoCreator extends DefaultJsonProtocol {
  implicit val todoCreatorFormat = jsonFormat2(TodoCreator.apply)
}