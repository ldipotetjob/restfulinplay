package com.ldg.model

import com.ldg.model.StatsTypes.{teamName, playerName}

/**
  * Created by ldipotet on 23/7/17.
  */
object SamplePlayer {

  def samplePlayers = List(Player("L.Neymar","F.C Barcelona"), Player("L.Suarez","F.C Barcelona"),
                     Player("L.Messy","F.C Barcelona"),Player("G. Bale","Real Madrid"), Player("C.Ronaldo","Real Madrid")
  )
}

case class Player(val playerName: playerName, val teamName: teamName)

