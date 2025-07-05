package ru.itmo.socket.client.util;

import ru.itmo.socket.common.entity.Route;
import ru.itmo.socket.common.entity.Location;

public class RoutePresenter {
  private final Localizator localizator;

  public RoutePresenter(Localizator localizator) {
    this.localizator = localizator;
  }

  public String describe(Route route) {
    String info = "";
    info += " ID: " + route.getId();
    info += "\n " + localizator.getKeyString("Name") + ": " + route.getName();
    info += "\n X: " + route.getCoordinates().getX();
    info += "\n Y: " + route.getCoordinates().getY();
    info += "\n " + localizator.getKeyString("CreationDate") + ": " + localizator.getDate(route.getCreationDate());
    info += "\n " + localizator.getKeyString("From") + describeLocation(route.getFrom());
    info += "\n " + localizator.getKeyString("To") + describeLocation(route.getTo());
    info += "\n " + localizator.getKeyString("Distance") + ": " + route.getDistance();


    return info;
  }

  public String describeLocation(Location location) {
    if (location == null) return ": null";

    String info = "";
    info += "\n    " + localizator.getKeyString("LocationName") + ": " + location.getName();
    info += "\n    " + localizator.getKeyString("LocationX") + ": " + location.getX();
    info += "\n    " + localizator.getKeyString("LocationY") + ": " + location.getY();
    info += "\n    " + localizator.getKeyString("LocationZ") + ": " + location.getZ();

    return info;
  }
}
