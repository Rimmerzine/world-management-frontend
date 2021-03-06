package models

sealed trait ErrorModel

object ErrorModel {

  final case object JsonParseError extends ErrorModel

  final case object UnexpectedStatus extends ErrorModel

  final case object CampaignNotFound extends ErrorModel

  final case object ElementNotFound extends ErrorModel

  final case object CreatureNotFound extends ErrorModel

}
