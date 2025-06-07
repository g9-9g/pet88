import { privateApi } from "./client";

export type NotificationStatus = "READ" | "UNREAD";

export interface Notification {
  id: number;
  userId: number;
  message: string;
  status: NotificationStatus;
}

export interface CreateNotificationDto {
  userId: number;
  message: string;
  status?: NotificationStatus;
}

export interface UpdateNotificationDto {
  userId?: number;
  message?: string;
  status?: NotificationStatus;
}

// Get all notifications
export const getAllNotifications = async (): Promise<Notification[]> => {
  const response = await privateApi.get<Notification[]>("/api/notifications");
  return response.data;
};

// Get owned notifications
export const getOwnedNotifications = async (
  status?: NotificationStatus
): Promise<Notification[]> => {
  const response = await privateApi.get<Notification[]>(
    "/api/notifications/owned",
    {
      params: { status },
    }
  );
  return response.data;
};

// Create a new notification
export const createNotification = async (
  data: CreateNotificationDto
): Promise<Notification> => {
  const response = await privateApi.post<Notification>(
    "/api/notifications",
    data
  );
  return response.data;
};

// Update a notification
export const updateNotification = async (
  id: number,
  data: UpdateNotificationDto
): Promise<Notification> => {
  const response = await privateApi.put<Notification>(
    `/api/notifications/${id}`,
    data
  );
  return response.data;
};
