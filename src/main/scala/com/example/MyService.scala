package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.httpx.SprayJsonSupport._
import Todo._
import TodoList._

class MyServiceActor extends Actor with MyService {

  def actorRefFactory = context

  def receive = runRoute(myRoute)

}

trait MyService extends HttpService with CORSSupport {

  val myRoute =
    cors {
        pathPrefix("todo") {
          pathEnd {
            get {
              respondWithMediaType(`application/json`) {
                complete {
                  print(todos)
                }
              }
            } ~
            post {
              respondWithMediaType(`application/json`)
              entity(as[TodoCreator]) { todo =>
                complete {
                  val id = getNextId
                  add(todo.create(id, todo.title, todo.order.getOrElse(id)))
                  print(findTodoById(id))
                }
              }
            } ~
            delete {
              respondWithMediaType(`application/json`)
              complete {
                TodoList.clear()
                print(todos)
              }
            }
          } ~ {
            path(Segment) { id =>
              get {
                respondWithMediaType(`application/json`) {
                  complete {
                    print(findTodoById(id.toInt))
                  }
                }
              } ~
              patch {
                respondWithMediaType(`application/json`)
                entity(as[TodoUpdate]) { todo =>
                  complete {
                    TodoList.update(id.toInt, todo)
                    print(findTodoById(id.toInt))
                  }
                }
              } ~
              delete {
                respondWithMediaType(`application/json`)
                complete {
                  TodoList.delete(id.toInt)
                  print(todos)
                }
              }
            }
          }
        }
    }
}
