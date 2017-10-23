import javax.inject._

import play.api.http.DefaultHttpFilters

import play.filters.cors.CORSFilter


// It can NOT be a singleton because it can not be instantiated => @Singleton

class Filters @Inject() (corsFilter: CORSFilter)
  extends DefaultHttpFilters(corsFilter)


