package utils

sealed trait ErrorModel

object ErrorModel {

  final case object JsonParseError extends ErrorModel

  final case object UnexpectedStatus extends ErrorModel

  final case object CampaignNotFound extends ErrorModel

  final case object PlaneNotFound extends ErrorModel

  final case object LandNotFound extends ErrorModel

}
