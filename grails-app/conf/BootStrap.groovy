import cmcw.VideoType

class BootStrap {

  def catalogService

  def init = { servletContext ->

    catalogService.importCatalog('/var/lib/tomcat6/webapps/smallcatalog.xml')
    new VideoType(netflixIdentifier:'movies').save()
  }
  def destroy = {
  }
}
