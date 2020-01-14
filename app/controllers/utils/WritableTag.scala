package controllers.utils

import play.api.http.{ContentTypeOf, ContentTypes, Writeable}
import play.api.mvc.Codec
import scalatags.Text.Tag

trait WritableTag {

  implicit def tagWritable(implicit codec: Codec): Writeable[Tag] = {
    Writeable(data => codec.encode("<!DOCTYPE html>" + data.render))
  }

  implicit def contentType(implicit codec: Codec): ContentTypeOf[Tag] = {
    ContentTypeOf(Some(ContentTypes.HTML))
  }

}
