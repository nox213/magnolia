package magnolia.examples

import scala.language.existentials
import scala.language.higherKinds

import magnolia._
import scala.language.experimental.macros

trait Default[T] { def default: T }

object Default {
  type Typeclass[T] = Default[T]
  def join[T](ctx: JoinContext[Default, T]): Default[T] = new Default[T] {
    def default = ctx.construct { param => param.typeclass.default }
  }

  def dispatch[T](ctx: DispatchContext[Default, T])(): Default[T] = new Default[T] {
    def default: T = ctx.subtypes.head.typeclass.default
  }

  implicit val string: Default[String] = new Default[String] { def default = "" }
  implicit val int: Default[Int] = new Default[Int] { def default = 0 }
  implicit def generic[T]: Default[T] = macro Magnolia.generic[T]
}
