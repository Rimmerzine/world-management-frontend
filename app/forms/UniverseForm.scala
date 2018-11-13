package forms

import play.api.data.Form
import play.api.data.Forms._

object UniverseForm {

  val universeName = "UniverseName"

  val form = Form(
    single(universeName -> nonEmptyText)
  )
}