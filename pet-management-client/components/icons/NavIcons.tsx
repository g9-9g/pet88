import { IconType } from "react-icons";
import {
  MdDashboard,
  MdPets,
  MdSettings,
  MdMedicalServices,
  MdGroups,
} from "react-icons/md";
import { BsCalendar2Week } from "react-icons/bs";
import { GiDogBowl } from "react-icons/gi";

export const iconMap: { [key: string]: IconType } = {
  "/assets/icons/dashboard.svg": MdDashboard,
  "/assets/icons/calendar.svg": BsCalendar2Week,
  "/assets/icons/pet.svg": MdPets,
  "/assets/icons/services.svg": GiDogBowl,
  "/assets/icons/medical.svg": MdMedicalServices,
  "/assets/icons/staff.svg": MdGroups,
  "/assets/icons/settings.svg": MdSettings,
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
