package services

import javax.inject.Singleton

import models.Profile

@Singleton
class ProfileManager
  extends SessionValueManager[Profile]("profile")(Profile.reads, Profile.writes)