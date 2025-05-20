import { IconType } from "react-icons";
import {
  MdPets,
  MdGroups,
  MdMedicalServices,
  MdHotel,
  MdDashboard,
} from "react-icons/md";
import { BsCalendar2Week } from "react-icons/bs";
import { GiDogBowl } from "react-icons/gi";
import { IoMdNotifications } from "react-icons/io";

export const iconMap: { [key: string]: IconType } = {
  dashboard: MdDashboard,
  request: IoMdNotifications,
  calendar: BsCalendar2Week,
  pets: MdPets,
  users: MdGroups,
  services: GiDogBowl,
  hotel: MdHotel,
};

interface NavIconProps {
  icon: string;
  className?: string;
}

export default function NavIcon({ icon, className = "" }: NavIconProps) {
  const IconComponent = iconMap[icon];

  if (!IconComponent) {
    return null;
  }

  return <IconComponent className={className} />;
}
